package com.example.integration.utility;

import java.util.List;

public class SkillUtil {
    public static boolean containsAnyKeywords(String jobDescription, List<String> keywords) {
        if (jobDescription == null || keywords == null) {
            return false;
        }

        for (String keyword : keywords) {
            if (jobDescription.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAllKeywords(String jobDescription, List<String> keywords) {
        if (jobDescription == null || keywords == null) {
            return false;
        }

        for (String keyword : keywords) {
            if (!jobDescription.toLowerCase().contains(keyword.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
