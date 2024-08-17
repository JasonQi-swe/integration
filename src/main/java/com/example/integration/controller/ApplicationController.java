package com.example.integration.controller;

import com.example.integration.entity.Application;
import com.example.integration.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public List<Application> getAllApplications() {
        return applicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable Long id) {
        Optional<Application> application = applicationService.findById(id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Application createApplication(@RequestBody Application application) {
        return applicationService.save(application);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(@PathVariable Long id, @RequestBody Application applicationDetails) {
        Optional<Application> applicationOptional = applicationService.findById(id);
        if (applicationOptional.isPresent()) {
            Application applicationToUpdate = applicationOptional.get();
            applicationToUpdate.setJob(applicationDetails.getJob());
            applicationToUpdate.setTenant(applicationDetails.getTenant());
            applicationToUpdate.setCoverLetter(applicationDetails.getCoverLetter());
            return ResponseEntity.ok(applicationService.save(applicationToUpdate));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Long id) {
        if (applicationService.findById(id).isPresent()) {
            applicationService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tenant/{tenantId}")
    public List<Application> getApplicationsByTenantId(@PathVariable Long tenantId) {
        return applicationService.findByTenantId(tenantId);
    }
}
