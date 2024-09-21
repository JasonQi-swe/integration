package com.example.integration.service;

import com.example.integration.entity.Application;
import com.example.integration.entity.Job;
import com.example.integration.repository.ApplicationRepository;
import com.example.integration.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobService jobService;
    private final TenantRepository tenantRepository;


    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, JobService jobService, TenantRepository tenantRepository) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
        this.tenantRepository = tenantRepository;
    }

    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    public Optional<Application> findById(Long id) {
        return applicationRepository.findById(id);
    }

    public Application save(Application application) {
        List<Application> existingApplication = applicationRepository.findAllByTenantIdAndJobId(application.getTenant().getId(), application.getJob().getId());
        if(existingApplication.size() > 0){
            log.info("Application with jobid {} tenantId {} exists already", application.getJob().getId(), application.getTenant().getId());
            return existingApplication.get(0);
        }
        return applicationRepository.save(application);
    }

    public void deleteById(Long id) {
        applicationRepository.deleteById(id);
    }

    public List<Application> findByTenantId(Long tenantId) {
        return applicationRepository.findByTenantId(tenantId);
    }

    public List<Application> findAllByTenantIdAndDate(Long tenantId, LocalDate date) {
        return applicationRepository.findAllByTenantIdAndLocalDate(tenantId, date);
    }

    public List<Application> findAllByTenantIdAndJobId(Long tenantId, Long jobId){
        return applicationRepository.findAllByTenantIdAndJobId(tenantId, jobId);
    }

    public boolean isApplicationDuplicated(Long tenantId, Long jobId) {
        Optional<Job> jobOptional = jobService.findById(jobId);
        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            String jobTitle = job.getTitle();
            String jobCompany = job.getCompany();
            Optional<Application> duplicateApplication = applicationRepository.findDuplicateApplication(jobTitle, jobCompany, tenantId);
            return duplicateApplication.isPresent();
        } else {
            // Job not found, cannot check for duplication
            return false;
        }
    }
}
