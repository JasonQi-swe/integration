package com.example.integration.model.clientModel;

import lombok.Data;

import java.util.List;

@Data
public class GetJobDetailsResponse {
    private boolean success;
    private String message;
    private JobData data;

    @Data
    public static class JobData {
        private String id;
        private String state;
        private String title;
        private String description;
        private String url;
        private ApplyMethod applyMethod;
        private Company company;
        private String location;
        private String type;
        private int views;
        private boolean closed;
        private boolean workRemoteAllowed;
        private String workPlace;
        private long expireAt;
        private List<String> formattedJobFunctions;
        private List<String> jobFunctions;
        private List<Integer> industries;
        private List<String> formattedIndustries;
        private String formattedExperienceLevel;
        private long listedAt;
        private String listedAtDate;
        private long originalListedAt;
        private String originalListedDate;
        private ContentLanguage contentLanguage;

        @Data
        public static class ApplyMethod {
            private String companyApplyUrl;
            private String easyApplyUrl;
        }

        @Data
        public static class Company {
            private int id;
            private String name;
            private String universalName;
            private String description;
            private String logo;
            private String url;
            private int followerCount;
            private int staffCount;
            private StaffCountRange staffCountRange;
            private List<String> specialities;
            private List<String> industries;
            private Headquarter headquarter;

            @Data
            public static class StaffCountRange {
                private int start;
                private int end;
            }

            @Data
            public static class Headquarter {
                private String geographicArea;
                private String country;
                private String city;
                private String postalCode;
                private String line1;
                private String line2;
            }
        }

        @Data
        public static class ContentLanguage{
            private String code;
            private String name;
        }
    }
}
