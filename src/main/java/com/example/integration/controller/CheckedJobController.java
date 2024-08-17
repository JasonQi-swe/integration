package com.example.integration.controller;

import com.example.integration.entity.CheckedJob;
import com.example.integration.service.CheckedJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/checked-jobs")
public class CheckedJobController {

    private final CheckedJobService checkedJobService;

    @Autowired
    public CheckedJobController(CheckedJobService checkedJobService) {
        this.checkedJobService = checkedJobService;
    }

    @GetMapping
    public List<CheckedJob> getAllCheckedJobs() {
        return checkedJobService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckedJob> getCheckedJobById(@PathVariable Long id) {
        Optional<CheckedJob> checkedJob = checkedJobService.findById(id);
        return checkedJob.map(ResponseEntity::ok)
                         .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public CheckedJob createCheckedJob(@RequestBody CheckedJob checkedJob) {
        return checkedJobService.save(checkedJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckedJob> updateCheckedJob(@PathVariable Long id, @RequestBody CheckedJob checkedJobDetails) {
        Optional<CheckedJob> checkedJobOptional = checkedJobService.findById(id);
        if (checkedJobOptional.isPresent()) {
            CheckedJob checkedJobToUpdate = checkedJobOptional.get();
            checkedJobToUpdate.setJob(checkedJobDetails.getJob());
            checkedJobToUpdate.setTenant(checkedJobDetails.getTenant());
            return ResponseEntity.ok(checkedJobService.save(checkedJobToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckedJob(@PathVariable Long id) {
        if (checkedJobService.findById(id).isPresent()) {
            checkedJobService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tenant/{tenantId}")
    public List<CheckedJob> getCheckedJobsByTenantId(@PathVariable Long tenantId) {
        return checkedJobService.findByTenantId(tenantId);
    }

    @GetMapping("/job/{jobId}")
    public List<CheckedJob> getCheckedJobsByJobId(@PathVariable Long jobId) {
        return checkedJobService.findByJobId(jobId);
    }

    @GetMapping("/tenant/{tenantId}/job/{jobId}")
    public Optional<CheckedJob> getCheckedJobsByTenantIdAndJobId(@PathVariable Long tenantId, @PathVariable Long jobId) {
        return checkedJobService.findByTenantIdAndJobId(tenantId, jobId);
    }
}
