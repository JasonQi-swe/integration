package com.example.integration.model.clientModel;

import com.example.integration.model.JobSummary;
import lombok.Data;

import java.util.List;

@Data
public class SearchJobsResponse {
    private boolean success;
    private String message;
    private List<JobSummary> data;
    private int total;
}
