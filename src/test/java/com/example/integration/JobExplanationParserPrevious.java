package com.example.integration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobExplanationParserPrevious {

    public static void main(String[] args) {
        String openaiResponse1 = "[3991338958: YES, the job requires expertise in data analytics, machine learning, and Python, all of which align with the CV's qualifications in applied AI and biomedical sciences. The requirement for knowledge in Power BI and SQL also matches well. The candidate’s experience with data analysis in healthcare makes this role a strong fit., 3989623632: NO, while the role involves data science and product insights, it focuses more on user experience and playback performance rather than the candidate's specialization in healthcare and biotechnology, making it less relevant., 3989865563: YES, this position requires skills in machine learning, data analysis, and cloud solutions, which perfectly aligns with the candidate's background in applied AI and biomedical sciences. The focus on analyzing sensor data and machine learning applications in agriculture also complements their skills., 3989587038: YES, the role involves developing AI and machine learning solutions, which fits well with the candidate's expertise in these areas, especially in the context of operational challenges that could relate to health technology., 3989137401: NO, this role is more focused on data engineering and requires leadership experience, which does not align with the candidate's current level of experience or focus on AI and biomedical applications., 3991348646: NO, this internship is geared towards learning AI and data science from scratch, which may not be suitable for a candidate with advanced studies and experience in applied AI and biomedical sciences., 3989623632: NO, similar to the previous analysis, this role focuses on consumer experiences and does not leverage the candidate's expertise in AI and healthcare., 3989865563: YES, this job's responsibilities in data analysis, machine learning, and cloud solutions align with the candidate's skills and experience in healthcare applications, making it a strong fit., 3989587038: YES, the emphasis on AI and machine learning solutions directly relates to the candidate's advanced studies and experience in the field, particularly in healthcare contexts., 3989137401: NO, this position requires seniority and leadership in data science, which does not align with the candidate's current experience level., 3989137401: NO, as previously mentioned, the focus is on senior-level data science leadership, which does not match the candidate's experience or focus on AI in healthcare., 3990682253: NO, this role is focused on marketing data analysis rather than AI applications in healthcare, making it less relevant to the candidate's expertise., 3991353745: YES, the role involves AI and data science, which aligns with the candidate's qualifications. The focus on developing AI-driven solutions is particularly relevant to their background., 3990678654: NO, while this role involves machine learning, the emphasis on federated learning may not align with the candidate's specific expertise in healthcare applications., 3991344082: YES, similar to the previous roles, this position requires knowledge in AI and machine learning, which fits well with the candidate's advanced studies and experience., 3989104628: NO, this position focuses on Python programming and orchestration rather than directly applying AI in healthcare, making it less relevant., 3989111486: NO, this internship is aimed at learning opportunities in data science, which may not be suitable for a candidate with advanced studies and experience in applied AI and biomedical sciences., 3989108604: YES, this role involves data analysis and machine learning, which aligns well with the candidate's background in applied AI and healthcare, making it a good fit., 3990684275: NO, the focus on Snowflake and data architecture does not align with the candidate's specific interests in AI and healthcare applications., 3989844332: NO, while the role involves AI, the emphasis on pharmaceutical experience may not directly align with the candidate's background in biomedical sciences and healthcare applications., 3990678654: NO, this position is particularly focused on federated learning, which may not be the candidate's primary area of expertise., 3989104628: NO, this role requires extensive experience in Python programming and orchestration, which may not directly leverage the candidate's strengths in AI and healthcare., 3991350865: YES, the focus on AI and data engineering in healthcare aligns well with the candidate's background in applied AI and biomedical sciences, making this a suitable match., 3989108530: NO, while this position involves data analysis, it does not emphasize AI applications in healthcare, making it less relevant to the candidate's expertise., 3989135016: NO, this position is more focused on business analytics rather than directly applying AI in healthcare, which is the candidate's area of expertise.]";

        String openaiResponse = "[3987999194: YES, this job as an AI Engineer with LLM app development aligns perfectly with the CV's focus on machine learning and programming in Python, particularly in the context of healthcare applications. The experience with chatbot development also matches well with the responsibilities outlined in the job description. \n" +
                "\n" +
                "3988330687: NO, this internship in Data Science does not fit well because the position is unpaid and aimed at individuals currently pursuing a degree, while the CV reflects a more advanced academic and professional background, including a Ph.D. and extensive research experience.\n" +
                "\n" +
                "3987953771: NO, while the position as a Cyber Threat Intelligence Analyst involves technical skills, it primarily focuses on cybersecurity rather than artificial intelligence or biotechnology, which are the main interests expressed in the job hunting summary and CV.\n" +
                "\n" +
                "3988370291: YES, this Data Scientist role involves developing AI and machine learning solutions, which is a strong match for the CV's emphasis on machine learning techniques and experience with data analysis in healthcare contexts.\n" +
                "\n" +
                "3987967807: NO, this Data Engineering position is more focused on ETL processes and data warehousing, which does not directly align with the CV’s specialization in AI and biotechnology.\n" +
                "\n" +
                "3966561671: NO, while this job requires extensive Python experience, it is more focused on general programming and data management rather than the specific applications in AI or healthcare that the CV emphasizes.\n" +
                "\n" +
                "3989972988: NO, this Lead Data Engineer position is centered on Databricks and consulting, which does not align with the CV's focus on AI applications in biotechnology and healthcare.\n" +
                "\n" +
                "3987966747: NO, the Cryptography Engineer position is highly specialized and not related to AI or biotechnology, which are the primary areas of interest in the CV and job hunting summary.\n" +
                "\n" +
                "3989990833: NO, the Data Analyst role focuses on general data analysis and project management without a specific connection to AI, biotechnology, or healthcare, which are the main interests in the CV.\n" +
                "\n" +
                "3987967624: NO, this Data Analytics position is more focused on learning and broad data analysis skills rather than applying AI techniques in biotechnology or medical sciences, which is the CV's focus.\n" +
                "\n" +
                "3987970198: YES, this job in Data Science aligns well with the CV's focus on machine learning and data analysis, particularly in healthcare and AI applications, making it a good fit for the candidate's background.]";
        Map<String, String> jobExplanationMap = parseJobExplanations(openaiResponse);

        System.out.println(jobExplanationMap.size());
        System.out.println(jobExplanationMap);
        //jobExplanationMap.forEach((jobId, explanation) ->
            //System.out.println("Job ID: " + jobId + ", Explanation: " + explanation));
    }

    public static Map<String, String> parseJobExplanations(String openaiResponse) {
        openaiResponse = openaiResponse.substring(1, openaiResponse.length() - 1);

        Pattern pattern = Pattern.compile("(\\d+):\\s*(YES|NO),\\s*(.*?)(?=(\\n\\d+:\\s*(YES|NO)|$))", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(openaiResponse);

        Map<String, String> jobExplanationMap = new HashMap<>();

        while (matcher.find()) {
            String jobId = matcher.group(1).trim();
            String explanation = matcher.group(2).trim() + ", " + matcher.group(3).trim();
            jobExplanationMap.put(jobId, explanation);
        }

        return jobExplanationMap;
    }
}
