package com.example.integration.controller;

import com.example.integration.entity.Job;
import com.example.integration.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Optional<Job> job = jobService.findById(id);
        return job.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Job createJob(@RequestBody Job job) {
        return jobService.save(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        Optional<Job> jobOptional = jobService.findById(id);
        if (jobOptional.isPresent()) {
            Job jobToUpdate = jobOptional.get();
            jobToUpdate.setTitle(jobDetails.getTitle());
            jobToUpdate.setState(jobDetails.getState());
            jobToUpdate.setDescription(jobDetails.getDescription());
            jobToUpdate.setUrl(jobDetails.getUrl());
            jobToUpdate.setCompany(jobDetails.getCompany());
            jobToUpdate.setLocation(jobDetails.getLocation());
            jobToUpdate.setType(jobDetails.getType());
            jobToUpdate.setClosed(jobDetails.isClosed());
            jobToUpdate.setListedAtDate(jobDetails.getListedAtDate());
            jobToUpdate.setFormattedExperienceLevel(jobDetails.getFormattedExperienceLevel());
            return ResponseEntity.ok(jobService.save(jobToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        if (jobService.findById(id).isPresent()) {
            jobService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/company/{company}")
    public List<Job> getJobsByCompany(@PathVariable String company) {
        return jobService.findByCompany(company);
    }

    @GetMapping("/location/{location}")
    public List<Job> getJobsByLocation(@PathVariable String location) {
        return jobService.findByLocation(location);
    }

    @GetMapping("/type/{type}")
    public List<Job> getJobsByType(@PathVariable String type) {
        return jobService.findByType(type);
    }
}
