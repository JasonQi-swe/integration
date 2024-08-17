package com.example.integration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobExplanationParser4Mini {

    public static void main(String[] args) {
        String s1 ="[123123:eerrrr]";
        String s2 = "[3991338958: YES, the job requires expertise in data analytics, machine learning, and Python, all of which align with the CV's qualifications in applied AI and biomedical sciences. The requirement for knowledge in Power BI and SQL also matches well. The candidate’s experience with data analysis in healthcare makes this role a strong fit., 3989623632: NO, while the role involves data science and product insights, it focuses more on user experience and playback performance rather than the candidate's specialization in healthcare and biotechnology, making it less relevant., 3989865563: YES, this position requires skills in machine learning, data analysis, and cloud solutions, which perfectly aligns with the candidate's background in applied AI and biomedical sciences. The focus on analyzing sensor data and machine learning applications in agriculture also complements their skills., 3989587038: YES, the role involves developing AI and machine learning solutions, which fits well with the candidate's expertise in these areas, especially in the context of operational challenges that could relate to health technology., 3989137401: NO, this role is more focused on data engineering and requires leadership experience, which does not align with the candidate's current level of experience or focus on AI and biomedical applications.]";
        String s3 ="[39747776022: YES, this job is a great fit because it heavily involves AI/ML engineering, which aligns perfectly with the candidate's expertise in applied artificial intelligence and machine learning techniques, particularly in Python and relevant libraries like TensorFlow and PyTorch. The collaborative and agile environment described also matches the candidate's experience in interdisciplinary settings. Additionally, the focus on optimizing AI models and maintaining high standards resonates with the candidate's academic and research background in biomedical sciences.], [399134450: NO, this job does not fit well with the CV and hunting summary because it is primarily focused on cybersecurity and digital marketing, which are outside the candidate's stated interest in artificial intelligence and biotechnology. The role emphasizes DevSecOps and application security testing, areas that do not align with the candidate's expertise in machine learning and healthcare applications.]";

        String s4 ="[3991307498: YES, this position as an AWS Cloud Security Specialist aligns well with the CV which highlights expertise in machine learning and data analysis, indicating strong analytical skills that are crucial for ensuring information security in cloud environments. The emphasis on working autonomously and in teams also resonates with the candidate’s experience in interdisciplinary collaboration.], [3991348646: YES, the AI, Data Science, DevOps, and Cloud Intern position is a perfect fit as it combines AI, data science, and cloud technologies, directly aligning with the candidate's educational background and skills in machine learning and Python programming. The focus on hands-on experience and innovative projects matches the candidate's research experience in biotechnology and healthcare.], [3991350079: YES, the Data Scientist role requires skills in data analytics and machine learning, which are core strengths of the candidate. The position's emphasis on ETL tasks and statistical analysis aligns with the candidate's experience in data processing and analysis in biomedical research.]";
        String s5 = "[3988337927: NO - The job focuses on cryptography and data encryption, which may not align with your interest in Java, Python, or Angular. Additionally, it lacks remote flexibility.], [3987339658: NO - This job requires mobile testing expertise and experience with specific tools that are not mentioned in your key requirements. It also involves senior-level responsibilities.], [3991520047: NO - The role requires extensive experience primarily in Node.js, which is outside your preferred programming languages. It also appears to be more senior than you are looking for.], [3991384376: NO - This position demands significant experience in cloud platform engineering with a focus on DevOps tools, which may not fit your experience level or language preference.], [3986944215: NO - The job focuses heavily on data warehousing and Azure services, which may not align with your programming language preferences. It also requires substantial experience.], [3989193009: NO - The position requires strong integration experience with IIB/ACE and MQ, which does not align with your focus on Java, Python, or Angular.], [3988358787: NO - This job requires collecting corporate emails, which does not involve programming or software development roles.], [3991260153: NO - This role focuses on Node.js and AWS, which are not part of your preferred technologies. It also seems to require more experience than you possess.], [3988047689: NO - This position requires mechanical skills and support experience rather than software development roles.], [3990245647: YES - This AWS Solutions Architect role is remote and focuses on cloud solutions, which can involve Java and Python. It may not be too senior depending on your experience.], [3991516564: NO - This OIC Developer role emphasizes supply chain experience and does not align with your programming language preferences.], [3988601423: NO - The role requires extensive experience with Android development, which is not aligned with your preferred programming languages.], [3988343440: NO - This job focuses on Android development and requires experience with technologies outside of your preferred stack.], [3988349009: NO - This position emphasizes software integration with a focus on SQL automation, which may not align well with your programming language preferences.], [3986137823: NO - This role requires extensive experience in .NET and related frameworks, which does not meet your programming language criteria.], [3990274541: YES - This Senior iOS Developer position is remote and while it focuses on Swift, it may provide opportunities to work with Java or Python in a supportive environment.], [3989950347: NO - This position is heavily focused on Java and requires significant experience, which doesn’t align with your level of expertise.], [3988041976: NO - This job emphasizes IoT and Azure development, which may not match your preferred programming languages.], [3987625025: NO - The role requires deep experience in GCP and Java frameworks, which may not fit your current skill level.], [3990248414: NO - This Java Full Stack Developer position is focused on technologies outside your preferred programming languages.], [3988825905: YES - This Data Developer role focuses on Python and data analytics, which aligns well with your programming preferences and is remote.], [3991545295: NO - This .Net Developer role does not align with your preferred programming languages or remote work criteria.], [3974657587: NO - This position focuses on translation services, which does not involve software development.], [3987106842: NO - This AEM Developer role requires specific experience in Adobe Experience Manager, which does not match your programming language preferences.], [3988433175: YES - This Quality Assurance Automation role involves testing and may provide opportunities to work with Java or Python in a remote setting.]\n";
        String s6 = "[3988827726: NO, While this job is remote and requires Python, it also demands extensive experience with AWS Glue and other AWS services, which might be too senior for you. Additionally, it involves data warehousing, which might not align with your preference for Java, Python, or Angular.]\n" +
                "\n" +
                "[3991520047: NO, This job requires significant Node.JS experience as the primary language, which does not align with your preference for Java, Python, or Angular.]\n" +
                "\n" +
                "[3990954265: YES, This job is remote and focuses on Java and Spring Boot, which are within your preferred languages. The job requires experience in building APIs and web services, but it does not specify a need for senior or principal roles, making it a good fit.]\n" +
                "\n" +
                "[3986103606: NO, Similar to job 3988827726, this role requires extensive experience with AWS Glue and other AWS services, which may be too senior for you. It also focuses heavily on data warehousing.]\n" +
                "\n" +
                "[3986995701: YES, This job is remote and focuses on Java and AWS, which are within your preferred languages. It does not specify a requirement for senior or principal roles, making it a good fit.]\n" +
                "\n" +
                "[3988041976: NO, This job requires extensive experience with .NET Core and Azure, which are not within your preferred languages of Java, Python, or Angular.]\n" +
                "\n" +
                "[3990248414: NO, This job requires experience with both Java and Node.js, with a significant emphasis on Node.js, which does not align with your preference for Java, Python, or Angular.]\n" +
                "\n" +
                "[3990951479: YES, This job is remote and focuses on Java and Spring Boot, which are within your preferred languages. It does not specify a requirement for senior or principal roles, making it a good fit.]\n" +
                "\n" +
                "[3988397535: NO, This job requires significant experience with React and Node.js, which does not align with your preference for Java, Python, or Angular.]\n" +
                "\n" +
                "[3986137823: NO, This job requires extensive experience with .NET Framework and other technologies outside your preferred languages of Java, Python, or Angular.]";
        System.out.println(parseJobExplanations(s1).size());
        System.out.println(parseJobExplanations(s2).size());//expected 5.
        System.out.println(parseJobExplanations(s3).size());//expected 2.
        System.out.println(parseJobExplanations(s4).size()); //expected 3.
        System.out.println(parseJobExplanations(s5).size());
        System.out.println(parseJobExplanations(s6).size());

        System.out.println();
        System.out.println(isValidFormat(s1));
        System.out.println(isValidFormat(s2));
        System.out.println(isValidFormat(s3));
        System.out.println(isValidFormat(s4));
        System.out.println(isValidFormat(s5));
        System.out.println(isValidFormat(s6));
    }
    public static Map<String, String> parseJobExplanations(String input) {
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
    public static boolean isValidFormat(String input) {
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

    public static boolean isValidFormatold(String input) {
        input = input.trim();
        if (!(input.startsWith("[") && input.endsWith("]"))) {
            return false;
        }

        input = input.substring(1, input.length() - 1);

        String[] entries = input.split("\\],\\s*\\[");

        for (String entry : entries) {
            if (!entry.matches("\\d{9,11}:\\s*(YES|NO),\\s*.+")) {
                return false;
            }
        }

        return true;
    }
}
