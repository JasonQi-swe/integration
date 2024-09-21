package com.example.integration.service;

import com.example.integration.enumerator.ExperienceLevel;
import com.example.integration.enumerator.LocationEnum;
import com.example.integration.enumerator.searchJobsRequest.DatePosted;
import com.example.integration.enumerator.JobType;
import com.knuddels.jtokkit.api.Encoding;
import com.example.integration.entity.Application;
import com.example.integration.entity.CheckedJob;
import com.example.integration.entity.Job;
import com.example.integration.entity.Tenant;
import com.example.integration.enumerator.OnsiteRemote;
import com.example.integration.model.clientModel.GetJobDetailsResponse;
import com.example.integration.model.Answer;
import com.example.integration.model.JobSummary;
import com.example.integration.model.clientModel.SearchJobsRequest;
import com.example.integration.model.Question;
import com.example.integration.utility.SkillUtil;
import com.knuddels.jtokkit.api.ModelType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class IntegrationService {

    @Value("${high_performance_mode:false}")
    private boolean HIGH_PERFORMANCE_MODE;

    private final LinkedInService linkedInService;
    private final AIService aiService;
    private final CheckedJobService checkedJobService;
    private final JobService jobService;
    private final ApplicationService applicationService;
    private final WebDriverService webDriverService;
    private final Encoding encoding;
    private final ModelType aiModel;
    private ThreadLocal<Integer> foundTargetedJobs = ThreadLocal.withInitial(() -> 0);

    private final int JOB_MAP_SIZE_TO_CHECK = 5;

    public IntegrationService(LinkedInService linkedInService,
                         AIService aiService,
                         CheckedJobService checkedJobService,
                         JobService jobService,
                         ApplicationService applicationService,
                         WebDriverService webDriverService,
                         Encoding encoding,
                         @Value("${openai.model}") String model) {
        this.linkedInService = linkedInService;
        this.aiService = aiService;
        this.checkedJobService = checkedJobService;
        this.jobService = jobService;
        this.applicationService = applicationService;
        this.webDriverService = webDriverService;
        this.encoding = encoding;
        this.aiModel = ModelType.fromName(model).get();
    }


    public boolean processJobs(Tenant tenant) throws GeneralSecurityException, IOException {
        String userName = tenant.getUserName();
        String cv = tenant.getCv();
        String summary = tenant.getSummary();
        Long tenantId = tenant.getId();
        String screeningKeywords = tenant.getScreeningSkills();
        String searchingKeyWords = tenant.getSearchingKeyWords();
        Integer totalTargetJobNumber = tenant.getTotalTargetJobNumber();
        LocationEnum location = tenant.getLocation();
        SearchJobsRequest request = createSearchJobsRequest(searchingKeyWords, location);

        int totalCountTest = 0;
        int page = 0;
        Map<String, Job> checkedJobsMap = new HashMap<>();
        while (foundTargetedJobs.get() <= totalTargetJobNumber) {
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
                boolean isLastJobAndMapEmpty = i == jobs.size() - 1 && checkedJobsMap.isEmpty();

//                if (!passesScreening(jobEntity, screeningKeywords) && isNotLastJob) {
//                    saveCheckedJob(tenant, jobEntity, "Does not pass keywords screening");
//                    log.info("The job {} did not pass the screening", jobId);
//                    continue;
//                }

                if (isJobChecked && (isNotLastJob || isLastJobAndMapEmpty)) {
                    log.info("The job {} has been checked already", jobId);
                    continue;
                }

                checkedJobsMap.put(jobId, jobEntity);

                if (checkedJobsMap.size() == JOB_MAP_SIZE_TO_CHECK || i==jobs.size()-1) {
                    processTwentyJobs(tenant, cv, summary, checkedJobsMap, userName, checkedJobsMap.size());
                    if (foundTargetedJobs.get() >= totalTargetJobNumber) {
                        //createReport(tenant);
                        log.info("There are total {} jobs have been targeted", foundTargetedJobs);
                        log.info("There are total {} jobs have been checked", totalCountTest);
                        return true;
                    }else {
                        checkedJobsMap.clear();
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

            Map<String, Job> checkedJobsMap = new HashMap<>();
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

                checkedJobsMap.put(jobIds.get(i), jobEntity);

                if (checkedJobsMap.size() == 25 || i==jobIds.size()-1) {
                    processTwentyJobs(tenant, cv, summary, checkedJobsMap, userName, checkedJobsMap.size());
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

    private SearchJobsRequest createSearchJobsRequest(String searchingKeyWords, LocationEnum location) {
        SearchJobsRequest request = new SearchJobsRequest();
        request.setKeywords(searchingKeyWords);

        request.setLocationId(location.getLocationId());
        //request.setSort(SortOption.mostRelevant.name());
        request.setDatePosted(DatePosted.pastMonth.name());
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
                job.setType(JobType.valueOf(jobDetail.getData().getType()));
                job.setExperienceLevel(ExperienceLevel.valueOf(jobDetail.getData().getFormattedExperienceLevel()));
                job.setLanguageName(jobDetail.getData().getContentLanguage().getName());
                job.setSource("API");
            }else{
                String url = "https://www.linkedin.com/jobs/view/"+jobId;
                Map<String, String> map = webDriverService.getJobDetail(url);

                job.setUrl(url);
                job.setId(Long.valueOf(jobId));
                job.setSource("WedDriver");
                job.setDescription(map.get("description"));
                job.setTitle(map.get("title"));
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
            jobEntity.setType(JobType.valueOf(data.getType()));
            jobEntity.setExperienceLevel(ExperienceLevel.valueOf(data.getFormattedExperienceLevel()));
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
            return !SkillUtil.containsAnyKeywords(jobEntity.getDescription(), firstScreenKeywords);
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

    private void processTwentyJobs(Tenant tenant, String cv, String summary, Map<String, Job> checkedJobsMap, String userName, int size) throws IOException {
        StringBuilder combinedQuestion = buildCombinedQuestion(tenant, checkedJobsMap, summary, cv);
        String question = combinedQuestion.toString();
        log.info("{} Combined Question:\n{}", userName, question);

        int tokenCount = encoding.countTokens(question);
        if (tokenCount > 127000) {
            log.error("Current question token {} is more than the maximum.", tokenCount);
        } else {
            log.info("tokenCount: {}", tokenCount);
            Answer answer = aiService.getAnswer(new Question(question), aiModel);
            log.debug("answer: {}", answer.answer().toString() );
            processAIResponse(answer, tenant, checkedJobsMap, size);
        }
    }

    private StringBuilder buildCombinedQuestion(Tenant tenant, Map<String, Job> checkedJobsMap, String summary, String cv) {
        StringBuilder combinedQuestion = null;
        if(tenant.getUserName().contains("example")){
            combinedQuestion = new StringBuilder(String.format("Here are %s jobs, my job hunting key requirement. Here is an example:[3990951479: YES, This job is a remote position and involves working with Java, which fits your requirement. It does not specify the need for a leadership role, making it suitable for you.], in which '3990951479' is the jobid \n" +
                    "\n" +
                    "[3988397535: NO, This job requires proficiency in React and Node.js, which does not align with your preference for working with Java, Python, or Angular (Typescript).].  In which, the explanation should include the string 'YES' if the job description fit my hunting key requirement very well, and explain why the job is a good fit. Also explain why a job is not fit. There are %s jobs in total, so please answer for every job(namely, your answer should contain %s YES or NO).  \n\n", checkedJobsMap.size(), checkedJobsMap.size(), checkedJobsMap.size()));
            combinedQuestion.append(String.format("\nHunting key requirement: %s", summary));
        }else {
            combinedQuestion = new StringBuilder(String.format("Here are %s job descriptions. PLEASE answer in this format exactly: [jobId1: explanation1, jobId2: explanation2, jobId3: explanation3, jobId4: explanation4](This is the format, so please do not actual use jobId1 or jobId1234567890 and so on) Here is an example:[3990951479: YES, This job is a remote position and involves working with Java, which fits your requirement. It does not specify the need for a leadership role, making it suitable for you.]\\n\" +\n" +
                    "                    \"\\n\" +\n" +
                    "                    \"[3988397535: NO, This job requires proficiency in React and Node.js, which does not align with your preference for working with Java, Python, or Angular (Typescript).].  In which, the explanation should include the string 'YES' if the job description fit my CV and additional hunting requirement very well, and explain why the job is a good fit. Also explain why a job is not fit. There are %s jobs in total, so please answer for every job(namely, your answer should contain in total %s 'YES' or 'NO').   \n\n", checkedJobsMap.size(), checkedJobsMap.size(), checkedJobsMap.size()));
            combinedQuestion.append(String.format("\n\nCV: %s\n\n", cv));

            combinedQuestion.append(String.format("\n\nAdditional hunting requirement: %s\n\n", summary));
        }
        for (Map.Entry<String, Job> entry : checkedJobsMap.entrySet()) {
            combinedQuestion.append(String.format("Job ID: %s, Description: '%s'.\n", entry.getKey(), entry.getValue().getDescription()));
            combinedQuestion.append("---------------------------------------------\n");
        }


        return combinedQuestion;
    }

    private void processAIResponse(Answer answer, Tenant tenant, Map<String, Job> checkedJobsMap, int size) throws IOException {
        Long tenantId = tenant.getId();
        Map<String, String> jobExplanationMap = new HashMap<>();
        if(aiModel == ModelType.GPT_4O){
            jobExplanationMap = parseJobExplanations4o(answer.answer(), size);
        }else{
            jobExplanationMap = parseJobExplanations(answer.answer());
        }

        List<String> jobIdsWithYes = getJobIdsWithYesExplanation(jobExplanationMap);

        for (String positiveJobId : jobIdsWithYes) {
            String explanation = jobExplanationMap.get(positiveJobId);
            String coverLetter = tenant.getCoverLetter();
            log.info("Fit Job ID: {}, Explanation: {}", positiveJobId, explanation);
            if(tenant.isNeedCoverLetter()) {
                if (alreadyApplied(positiveJobId, tenantId)) {
                    log.info("Application with jobid {} tenantId {} exists already", positiveJobId, tenantId);
                } else if (isApplicationDuplicated(tenantId, positiveJobId)) {
                    log.info("Application with jobid {} tenantId {} is duplicated", positiveJobId, tenantId);
                } else {
                    String cv = tenant.getCv();
                    String userName = tenant.getUserName();
                    String q = buildCoverLetterQuestion(checkedJobsMap, cv, coverLetter, tenant.getRequirementsForCoverLetter(), positiveJobId, tenant.getUserName());
                    coverLetter = aiService.getAnswer(new Question(q), aiModel).answer();
                    log.debug("[{}] generated cover letter {} for jobid {}", userName, coverLetter, positiveJobId);
                }
            }
            foundTargetedJobs.set(foundTargetedJobs.get() + 1);
            saveApplication(tenant, checkedJobsMap, coverLetter, positiveJobId, explanation);
            saveCheckedJob(tenant, checkedJobsMap.get(positiveJobId), "Selected");
            jobExplanationMap.remove(positiveJobId);
        }


        for (Map.Entry<String, String> entry : jobExplanationMap.entrySet()) {
            String jobId = entry.getKey();
            String explanation = entry.getValue();
            saveCheckedJob(tenant, checkedJobsMap.get(jobId), explanation);
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

    private String buildCoverLetterQuestion(Map<String, Job> checkedJobsMap, String cv, String coverLetter, String requirementsForCoverLetter, String positiveJobId, String userName) {

        String question = "";
        if(userName.toLowerCase().contains("example")){
            question = String.format(
                    "Based on the job '%s' and my cv '%s', write a simple cover letter for me. %s . Also, Do NOT include these: **[Your Name]** [Your Address] [City, State, ZIP Code] [Email Address] [Phone Number] [Date] [Company Address] [City, State, ZIP Code]. And exclude these kinds of unnecessary words: \"Certainly! Here's a simple and concise cover letter tailored to the job description provided:\"  or \"Feel free to customize this cover letter further to better match your personal style and experiences.\"",
                    checkedJobsMap.get(positiveJobId).getDescription(), cv, requirementsForCoverLetter
            );
        }else{
            question = String.format(
                    "Based on the job '%s' and my cv '%s', write a simple short cover letter. If the job description is in Italian, please write the cover letter in Italian too. Here is a cover letter I worte before: ''' % s''' And exclude these kinds of unnecessary words: \"Certainly! Here's a simple and concise cover letter tailored to the job description provided:\"  or \"Feel free to customize this cover letter further to better match your personal style and experiences.\"",
                    checkedJobsMap.get(positiveJobId).getDescription(), cv, coverLetter
            );
        }
        return question;
    }

    private void saveApplication(Tenant tenant, Map<String, Job> checkedJobsMap, String coverLetter, String positiveJobId, String jobIdToReason) {
        Application application = new Application();
        application.setJob(checkedJobsMap.get(positiveJobId));
        application.setTenant(tenant);
        application.setCoverLetter(coverLetter);
        application.setReasonToSelect(jobIdToReason);
        applicationService.save(application);
    }

    private boolean alreadyApplied(String positiveJobId, Long tenantId) {
        return applicationService.findAllByTenantIdAndJobId(tenantId, Long.valueOf(positiveJobId)).size() > 0;
    }

    private  Map<String, String> parseJobExplanations4o(String input, int JOB_MAP_SIZE_TO_CHECK) {
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

        if(JOB_MAP_SIZE_TO_CHECK != result.size()) {
            log.error("checkedJobMap Size: {}, but expected size: {}. Here is the original response: {}", result.size(), JOB_MAP_SIZE_TO_CHECK, input);
            result.forEach((jobId, explanation) -> {
                log.error("Job ID: {}, Explanation: {}", jobId, explanation);
            });
            throw new RuntimeException("Job explanation map size does not match the expected size. Stopping process.");
        }

        return result;
    }

    private boolean isValidFormat4o(String input) {
        // Remove leading and trailing whitespace
        input = input.trim();

        // Check if the string starts with '[' and ends with ']'
        if (!(input.startsWith("[") && input.endsWith("]"))) {
            return false;
        }

        // Remove the outer brackets
        input = input.substring(1, input.length() - 1);

        // Split the string by "]\n\n[" to separate multiple entries
        String[] entries = input.split("\\]\\s*\\n\\s*\\n\\s*\\[");

        // Create a Pattern object with DOTALL flag
        Pattern pattern = Pattern.compile("\\d{9,11}:\\s*(YES|NO),\\s*.+", Pattern.DOTALL);

        for (String entry : entries) {
            // Check if each entry matches the expected format
            if (!pattern.matcher(entry).matches()) {
                return false;
            }
        }

        return true;
    }
}
