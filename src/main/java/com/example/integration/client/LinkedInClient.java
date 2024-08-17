package com.example.integration.client;

import com.example.integration.client.config.LinkedInClientConfig;
import com.example.integration.model.clientModel.GetJobDetailsResponse;
import com.example.integration.model.clientModel.SearchJobsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "linkedinClient", url = "${linkedin.api.url}", configuration = LinkedInClientConfig.class)
public interface LinkedInClient {

    @GetMapping("/search-jobs-v2")
    SearchJobsResponse searchJobs(@RequestParam Map<String, String> queryParams);

    @GetMapping("/get-job-details")
    GetJobDetailsResponse getJobDetails(@RequestParam("id") String jobId);

    //@GetMapping("/search-locations")

}
