package com.example.integration.controller;

import com.example.integration.model.clientModel.GetJobDetailsResponse;
import com.example.integration.model.JobSummary;
import com.example.integration.model.clientModel.SearchJobsRequest;
import com.example.integration.service.LinkedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LinkedInController {

    @Autowired
    private LinkedInService linkedinService;

    @PostMapping("/search-jobs")
    public List<JobSummary> searchJobs(@RequestBody SearchJobsRequest searchJobsRequest) {
        return linkedinService.searchJobs(searchJobsRequest);
    }

    @GetMapping("/get-job-details")
    public GetJobDetailsResponse getJobDetails(@RequestParam String jobId) {
        return linkedinService.getJobDetails(jobId);
    }
}
