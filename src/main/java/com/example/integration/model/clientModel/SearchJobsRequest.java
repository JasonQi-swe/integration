package com.example.integration.model.clientModel;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class SearchJobsRequest {

    private String keywords;  // Required
    private String locationId;  // Optional
    private String companyIds;  // Optional
    private String datePosted;  // Optional
    private String salary;  // Optional
    private String jobType;  // Optional
    private String experienceLevel;  // Optional
    private String titleIds;  // Optional
    private String functionIds;  // Optional
    private int page;  // Optional
    private String industryIds;  // Optional
    private String onsiteRemote;  // Optional
    private String sort;  // Optional

    public Map<String, String> toMap() {
        if (keywords == null || keywords.isEmpty()) {
            throw new IllegalArgumentException("Keywords are required");
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("keywords", keywords);
        if (locationId != null) queryParams.put("locationId", locationId);
        if (companyIds != null) queryParams.put("companyIds", companyIds);
        if (datePosted != null) queryParams.put("datePosted", datePosted);
        if (salary != null) queryParams.put("salary", salary);
        if (jobType != null) queryParams.put("jobType", jobType);
        if (experienceLevel != null) queryParams.put("experienceLevel", experienceLevel);
        if (titleIds != null) queryParams.put("titleIds", titleIds);
        if (functionIds != null) queryParams.put("functionIds", functionIds);
        if (page != 0) queryParams.put("start", Integer.toString(page*50));
        if (industryIds != null) queryParams.put("industryIds", industryIds);
        if (onsiteRemote != null) queryParams.put("onsiteRemote", onsiteRemote);
        if (sort != null) queryParams.put("sort", sort);
        return queryParams;
    }

}
