package com.example.integration.service;

import com.example.integration.client.LinkedInClient;
import com.example.integration.model.clientModel.GetJobDetailsResponse;
import com.example.integration.model.JobSummary;
import com.example.integration.model.clientModel.SearchJobsResponse;
import com.example.integration.model.clientModel.SearchJobsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LinkedInService {

    @Autowired
    private LinkedInClient linkedinClient;

    public List<JobSummary> searchJobs(SearchJobsRequest searchJobsRequest) {
        SearchJobsResponse response = linkedinClient.searchJobs(searchJobsRequest.toMap());
        List<JobSummary> jobList = Optional.ofNullable(response.getData()).orElse(new ArrayList<>());
        return jobList.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    public GetJobDetailsResponse getJobDetails(String jobId) {
        return linkedinClient.getJobDetails(jobId);
    }
}
