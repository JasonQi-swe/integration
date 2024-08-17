package com.example.integration;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobExplanationParser {

    public static void main(String[] args) {
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
        System.out.println(parseJobExplanations4o(s6).size());

        System.out.println();

        System.out.println(isValidFormat4o(s6));
    }
    public static Map<String, String> parseJobExplanations4o(String input) {
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

    public static boolean isValidFormat4o(String input) {
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
