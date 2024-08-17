package com.example.integration.service;

import com.knuddels.jtokkit.api.Encoding;
import com.example.integration.entity.Application;
import com.example.integration.entity.CheckedJob;
import com.example.integration.entity.Job;
import com.example.integration.entity.Tenant;
import com.example.integration.enumerator.searchJobsRequest.DatePosted;
import com.example.integration.enumerator.searchJobsRequest.JobType;
import com.example.integration.enumerator.searchJobsRequest.OnsiteRemote;
import com.example.integration.model.clientModel.GetJobDetailsResponse;
import com.example.integration.model.Answer;
import com.example.integration.model.JobSummary;
import com.example.integration.model.clientModel.SearchJobsRequest;
import com.example.integration.model.Question;
import com.example.integration.enumerator.AIModel;
import com.example.integration.utility.SkillUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class IntegrationService {

    @Autowired
    private LinkedInService linkedInService;

    @Autowired
    private AIService aiService;

    @Autowired
    private CheckedJobService checkedJobService;

    @Autowired
    private JobService jobService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private WebDriverService webDriverService;

    @Autowired
    private Encoding encoding;

    private int foundTargetedJobs = 0;

    private AIModel aiModel = AIModel.GPT_4o;

    private final static boolean HIGH_PERFORMANCE_MODE = false;

    public boolean processJobs(Tenant tenant) throws GeneralSecurityException, IOException {
        String userName = tenant.getUserName();
        String cv = tenant.getCv();
        String summary = tenant.getSummary();
        Long tenantId = tenant.getId();
        String screeningKeywords = tenant.getScreeningSkills();
        String searchingKeyWords = tenant.getSearchingKeyWords();
        Integer totalTargetJobNumber = tenant.getTotalTargetJobNumber();
        SearchJobsRequest request = createSearchJobsRequest(searchingKeyWords);

        int totalCountTest = 0;
        int page = 0;
        Map<String, Job> twentyJobsMap = new HashMap<>();
        while (foundTargetedJobs <= totalTargetJobNumber) {
            request.setPage(page++);
            log.info("page: {}", page);
            List<JobSummary> jobs = linkedInService.searchJobs(request);
            if (jobs.isEmpty()) {
                log.warn("No new jobs");
                break;
            } else {
                log.info("found new jobs: {}", jobs.size());
            }
            log.info("{} {}", userName, "jobs: " + jobs);


            for(int i =0; i< jobs.size(); i++){
                totalCountTest++;
                String jobId = jobs.get(i).getId();
                Job jobEntity = getOrCreateJob(jobId, jobs.get(i));

                boolean isJobChecked = isJobCheckedForTenant(tenantId, jobId);
                boolean isNotLastJob = i != jobs.size() - 1;
                boolean isLastJobAndMapEmpty = i == jobs.size() - 1 && twentyJobsMap.isEmpty();

                if (!passesScreening(jobEntity, screeningKeywords) && isNotLastJob) {
                    saveCheckedJob(tenant, jobEntity, "Does not pass keywords screening");
                    log.info("The job {} did not pass the screening", jobId);
                    continue;
                }

                if (isJobChecked && (isNotLastJob || isLastJobAndMapEmpty)) {
                    log.info("The job {} has been checked already", jobId);
                    continue;
                }

                twentyJobsMap.put(jobId, jobEntity);

                if (twentyJobsMap.size() == 10 || i==jobs.size()-1) {
                    processTwentyJobs(tenant, cv, summary, twentyJobsMap, userName);
                    if (foundTargetedJobs >= totalTargetJobNumber) {
                        //createReport(tenant);
                        log.info("There are total {} jobs have been targeted", foundTargetedJobs);
                        log.info("There are total {} jobs have been checked", totalCountTest);
                        return true;
                    }else {
                        twentyJobsMap.clear();
                    }
                }
            }
        }
        log.info("There are total {} jobs have been checked", totalCountTest);
        return false;
    }

    private boolean isApplicationDuplicated(Long tenantId, String jobId) {
        return applicationService.isApplicationDuplicated(tenantId, Long.valueOf(jobId));
    }

    public boolean processJobIds(Tenant tenant, List<String> jobIds) throws IOException {
        String userName = tenant.getUserName();
        String cv = tenant.getCv();
        String summary = tenant.getSummary();
        Long tenantId = tenant.getId();
        String screeningKeywords = tenant.getScreeningSkills();
        Integer totalTargetJobNumber = tenant.getTotalTargetJobNumber();

        int totalCountTest = 0;
        int page = 0;
        int foundTargetedJobs = 0;

        while (foundTargetedJobs <= totalTargetJobNumber) {

            log.info("page: {}", page);

            Map<String, Job> twentyJobsMap = new HashMap<>();
            for(int i =0; i< jobIds.size(); i++){
                totalCountTest++;
                Job jobEntity = getOrCreateJobWithoutJobSummary(jobIds.get(i));

                if (!passesScreening(jobEntity, screeningKeywords)) {
                    saveCheckedJob(tenant, jobEntity, "Does not pass keywords screening");
                    continue;
                }

                if (isJobCheckedForTenant(tenantId, jobIds.get(i))) {
                    continue;
                }

                twentyJobsMap.put(jobIds.get(i), jobEntity);

                if (twentyJobsMap.size() == 25 || i==jobIds.size()-1) {
                    processTwentyJobs(tenant, cv, summary, twentyJobsMap, userName);
                    if (foundTargetedJobs >= totalTargetJobNumber) {
                        //createReport(tenant);
                        log.info("There are total {} jobs have been targeted", foundTargetedJobs);
                        log.info("There are total {} jobs have been checked", totalCountTest);
                        return true;
                    }
                }
            }
        }
        log.info("There are total {} jobs have been checked", totalCountTest);
        return false;
    }

    private SearchJobsRequest createSearchJobsRequest(String searchingKeyWords) {
        SearchJobsRequest request = new SearchJobsRequest();
        request.setKeywords(searchingKeyWords);
        request.setLocationId("103644278"); //US
        //request.setLocationId("101174742"); //Canada
        //request.setLocationId("90010409");// stockholm
        //request.setSort(SortOption.mostRelevant.name());
        request.setDatePosted(DatePosted.pastWeek.name());
        request.setOnsiteRemote(OnsiteRemote.remote.name());
        request.setJobType(JobType.contract.name());
        return request;
    }

    private Job getOrCreateJob(String jobId, JobSummary jobSummary) throws IOException {
        Optional<Job> jobOptional = jobService.findById(Long.valueOf(jobId));
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        } else {
            Job job = new Job();
            if(HIGH_PERFORMANCE_MODE) {
                GetJobDetailsResponse jobDetail = linkedInService.getJobDetails(jobId);
                job = jobSummary.convertToJob();
                job.setDescription(jobDetail.getData().getDescription());
                job.setType(jobDetail.getData().getType());
                job.setFormattedExperienceLevel(jobDetail.getData().getFormattedExperienceLevel());
                job.setLanguageName(jobDetail.getData().getContentLanguage().getName());
                job.setSource("API");
            }else{
                String url = "https://www.linkedin.com/jobs/view/"+jobId;
                String description = webDriverService.getJobDetail(url);
                job.setUrl(url);
                job.setId(Long.valueOf(jobId));
                job.setSource("WedDriver");
                job.setDescription(description);
            }
            jobService.save(job);
            return job;
        }
    }

    private Job getOrCreateJobWithoutJobSummary(String jobId) throws IOException {
        Optional<Job> jobOptional = jobService.findById(Long.valueOf(jobId));
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        } else {
            GetJobDetailsResponse jobDetail = linkedInService.getJobDetails(jobId);
            GetJobDetailsResponse.JobData data = jobDetail.getData();
            Job jobEntity = new Job();
            jobEntity.setId(Long.valueOf(jobId));
            jobEntity.setTitle(data.getTitle());
            jobEntity.setUrl(data.getUrl());
            jobEntity.setCompany(data.getCompany().getName());
            jobEntity.setLocation(data.getLocation());
            jobEntity.setListedAtDate(LocalDateTime.parse(data.getListedAtDate()));
            jobEntity.setState(data.getState());
            jobEntity.setClosed(data.isClosed());
            jobEntity.setDescription(data.getDescription());
            jobEntity.setType(data.getType());
            jobEntity.setFormattedExperienceLevel(data.getFormattedExperienceLevel());
            jobEntity.setLanguageName(data.getContentLanguage().getName());
            jobService.save(jobEntity);
            return jobEntity;
        }
    }

    private boolean passesScreening(Job jobEntity, String screeningKeywords) {
        if(screeningKeywords == null){
            return true;
        }else {
            List<String> firstScreenKeywords = Collections.singletonList(screeningKeywords);
            return SkillUtil.containsAnyKeywords(jobEntity.getDescription(), firstScreenKeywords);
        }
    }

    private void saveCheckedJob(Tenant tenant, Job jobEntity, String reason) {
        CheckedJob checkedJob = new CheckedJob();
        checkedJob.setJob(jobEntity);
        checkedJob.setTenant(tenant);
        checkedJob.setReasonToSkip(reason);
        checkedJobService.save(checkedJob);
    }

    private boolean isJobCheckedForTenant(Long tenantId, String jobId) {
        return checkedJobService.findByTenantIdAndJobId(tenantId, Long.valueOf(jobId)).isPresent();
    }

    private void processTwentyJobs(Tenant tenant, String cv, String summary, Map<String, Job> twentyJobsMap, String userName) throws IOException {
        StringBuilder combinedQuestion = buildCombinedQuestion(tenant, twentyJobsMap, summary, cv);
        String question = combinedQuestion.toString();
        log.info("{} Combined Question:\n{}", userName, question);

        int tokenCount = encoding.countTokens(question);
        if (tokenCount > 127000) {
            log.error("Current question token {} is more than the maximum.", tokenCount);
        } else {
            log.info("tokenCount: {}", tokenCount);
            Answer answer = aiService.getAnswer(new Question(question), aiModel);
            log.debug("answer: {}", answer.answer().toString() );
            processAIResponse(answer, tenant, twentyJobsMap, cv, userName);
        }
    }

    private StringBuilder buildCombinedQuestion(Tenant tenant, Map<String, Job> twentyJobsMap, String summary, String cv) {
        StringBuilder combinedQuestion = null;
        if(tenant.getUserName().contains("example")){
            combinedQuestion = new StringBuilder(String.format("Here are %s jobs, my job hunting key requirement. PLEASE answer in this format exactly: [jobId1: explanation1, jobId2: explanation2, jobId3: explanation3, jobId4: explanation4](This is the format, so please do not actual use jobId1 or jobId1234567890 and so on) Here is an example:[3990951479: YES, This job is a remote position and involves working with Java, which fits your requirement. It does not specify the need for a leadership role, making it suitable for you.]\n" +
                    "\n" +
                    "[3988397535: NO, This job requires proficiency in React and Node.js, which does not align with your preference for working with Java, Python, or Angular (Typescript).].  In which, the explanation should include the string 'YES' if the job description fit my hunting key requirement very well, and explain why the job is a good fit. Also explain why a job is not fit. There are %s jobs in total, so please answer for every job(namely, your answer should contain %s YES or NO).  \n\n", twentyJobsMap.size(), twentyJobsMap.size(), twentyJobsMap.size()));
        }else {
            combinedQuestion = new StringBuilder(String.format("Here are my CV, %s jobs and my job hunting key requirement. PLEASE answer with this format [jobId1: explanation1, jobId2: explanation2, jobId3: explanation3](NOT this format: [jobId1: explanation1], [jobId2: explanation2], [jobId3: explanation3]).  In which, the explanation should include the string 'YES' if the job description fit my hunting key requirement and CV very well(my key requirement is more important than CV), and explain why the job is a good fit. Also explain why a job is not fit. There are %s jobs in total, so please answer for every job(namely, your answer should contain %s YES or NO).  \n\n", twentyJobsMap.size(), twentyJobsMap.size(), twentyJobsMap.size()));
            combinedQuestion.append(String.format("\n\nCV: %s\n\n", cv));
        }
        for (Map.Entry<String, Job> entry : twentyJobsMap.entrySet()) {
            combinedQuestion.append(String.format("Job ID: %s, Description: '%s'.\n", entry.getKey(), entry.getValue().getDescription()));
            combinedQuestion.append("---------------------------------------------\n");
        }
        combinedQuestion.append(String.format("\nHunting key requirement: %s", summary));

        return combinedQuestion;
    }

    private void processAIResponse(Answer answer, Tenant tenant, Map<String, Job> twentyJobsMap, String cv, String userName) throws IOException {
        Long tenantId = tenant.getId();
        Map<String, String> jobExplanationMap = new HashMap<>();
        if(aiModel == AIModel.GPT_4o){
            jobExplanationMap = parseJobExplanations4o(answer.answer());
        }else{
            jobExplanationMap = parseJobExplanations(answer.answer());
        }

        log.info("size: {}", jobExplanationMap.size());

        List<String> jobIdsWithYes = getJobIdsWithYesExplanation(jobExplanationMap);

        for (String positiveJobId : jobIdsWithYes) {
            String explanation = jobExplanationMap.get(positiveJobId);
            log.info("Fit Job ID: {}, Explanation: {}", positiveJobId, explanation);
            if (alreadyApplied(positiveJobId, tenantId)) {
                log.info("Application with jobid {} tenantId {} exists already", positiveJobId, tenantId);
            }else if(isApplicationDuplicated(tenantId, positiveJobId)){
                log.info("Application with jobid {} tenantId {} is duplicated", positiveJobId, tenantId);
            }else{
                foundTargetedJobs++;
                String q = buildCoverLetterQuestion(twentyJobsMap, cv, positiveJobId, tenant.getUserName());
                String coverLetter = aiService.getAnswer(new Question(q), AIModel.GPT_4o).answer();
                log.debug("[{}] generated cover letter {} for jobid {}", userName, coverLetter, positiveJobId);
                saveApplication(tenant, twentyJobsMap, coverLetter, positiveJobId, explanation);
            }
            saveCheckedJob(tenant, twentyJobsMap.get(positiveJobId), "Selected");
            jobExplanationMap.remove(positiveJobId);
        }

        for (Map.Entry<String, String> entry : jobExplanationMap.entrySet()) {
            String jobId = entry.getKey();
            String explanation = entry.getValue();

            saveCheckedJob(tenant, twentyJobsMap.get(jobId), explanation);
        }


    }

    private boolean isValidFormat(String input) {
        input = input.trim();

        if (!(input.startsWith("[") && input.endsWith("]"))) {
            return false;
        }

        input = input.substring(1, input.length() - 1);

        String[] entries = input.split("\\],\\s*\\[");

        for (String entry : entries) {
            if (!entry.matches("\\d{9,11}:\\s*(YES|NO)\\s*-?\\s*.+")) {
                return false;
            }
        }

        return true;
    }

    private Map<String, String> parseJobExplanations(String input) {

        if(!isValidFormat(input)){
            log.error("openai response is not valid for parsing: {}", input);
            throw new RuntimeException("openai response is not valid for parsing");
        }
        Map<String, String> result = new HashMap<>();
        input = input.replaceAll("^\\[|\\]$", "");
        String[] entries = input.split("\\], \\[");

        for (String entry : entries) {
            Matcher matcher = Pattern.compile("(\\d{9,11}):\\s*(.+?)(?=,\\s*\\d{10}:|$)").matcher(entry);

            while (matcher.find()) {
                String jobId = matcher.group(1);
                String reason = matcher.group(2).trim();

                reason = reason.replaceAll("[,\\]]$", "");

                result.put(jobId, reason);
            }
        }

        return result;
    }

    private List<String> getJobIdsWithYesExplanation(Map<String, String> jobExplanationMap) {
        return jobExplanationMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains("YES"))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    private String buildCoverLetterQuestion(Map<String, Job> twentyJobsMap, String cv, String positiveJobId, String userName) {

        String question = "";
        if(userName.toLowerCase().contains("example")){
            question = String.format(
"Also, Do NOT include these: **[Your Name]** [Your Address] [City, State, ZIP Code] [Email Address] [Phone Number] [Date] [Company Address] [City, State, ZIP Code]. And exclude these kinds of unnecessary words: \"Certainly! Here's a simple and concise cover letter tailored to the job description provided:\"  or \"Feel free to customize this cover letter further to better match your personal style and experiences.\"",
                    twentyJobsMap.get(positiveJobId).getDescription(), cv
            );
        }else{
            question = String.format(
                    "Based on the job '%s' and my cv '%s', write a simple short cover letter. Do NOT include these: **[Your Name]** [Your Address] [City, State, ZIP Code] [Email Address] [Phone Number] [Date] [Company Address] [City, State, ZIP Code]. And exclude these kinds of unnecessary words: \"Certainly! Here's a simple and concise cover letter tailored to the job description provided:\"  or \"Feel free to customize this cover letter further to better match your personal style and experiences.\"",
                    twentyJobsMap.get(positiveJobId).getDescription(), cv
            );
        }
        return question;
    }

    private void saveApplication(Tenant tenant, Map<String, Job> twentyJobsMap, String coverLetter, String positiveJobId, String jobIdToReason) {
        Application application = new Application();
        application.setJob(twentyJobsMap.get(positiveJobId));
        application.setTenant(tenant);
        application.setCoverLetter(coverLetter);
        application.setReasonToSelect(jobIdToReason);
        applicationService.save(application);
    }

    private boolean alreadyApplied(String positiveJobId, Long tenantId) {
        return applicationService.findAllByTenantIdAndJobId(tenantId, Long.valueOf(positiveJobId)).size() > 0;
    }

    private boolean isSameTitleFromSameCompany(Long tenantId, String jobTitle,String jobId){
        //applicationService.findAllByTenantIdAndJobTitle(tenantId, jobTitle);
        return true;
    }

    private  Map<String, String> parseJobExplanations4o(String input) {
        if(!isValidFormat4o(input)){
            log.error("openai response is not valid for parsing: {}", input);
            throw new RuntimeException("openai response is not valid for parsing");
        }
        Map<String, String> result = new HashMap<>();
        input = input.replaceAll("^\\[|\\]$", "");
        String[] entries = input.split("\\]\\s*\\n\\s*\\n\\s*\\[");

        for (String entry : entries) {
            Matcher matcher = Pattern.compile("(\\d{9,11}):\\s*(YES|NO),\\s*(.+)", Pattern.DOTALL).matcher(entry);

            if (matcher.find()) {
                String jobId = matcher.group(1);
                String yesNo = matcher.group(2);
                String reason = matcher.group(3).trim();

                reason = reason.replaceAll("\\]$", "");
                result.put(jobId, yesNo + ", " + reason);
            }
        }

        return result;
    }

    private boolean isValidFormat4o(String input) {
        input = input.trim();

        if (!(input.startsWith("[") && input.endsWith("]"))) {
            return false;
        }

        input = input.substring(1, input.length() - 1);

        String[] entries = input.split("\\]\\s*\\n\\s*\\n\\s*\\[");
        Pattern pattern = Pattern.compile("\\d{9,11}:\\s*(YES|NO),\\s*.+", Pattern.DOTALL);

        for (String entry : entries) {
            if (!pattern.matcher(entry).matches()) {
                return false;
            }
        }

        return true;
    }
}
