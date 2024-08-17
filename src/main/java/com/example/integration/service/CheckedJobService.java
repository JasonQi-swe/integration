package com.example.integration.service;

import com.example.integration.entity.CheckedJob;
import com.example.integration.entity.Job;
import com.example.integration.repository.CheckedJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CheckedJobService {

    private final CheckedJobRepository checkedJobRepository;

    @Autowired
    public CheckedJobService(CheckedJobRepository checkedJobRepository) {
        this.checkedJobRepository = checkedJobRepository;
    }

    public List<CheckedJob> findAll() {
        return checkedJobRepository.findAll();
    }

    public Optional<CheckedJob> findById(Long id) {
        return checkedJobRepository.findById(id);
    }

    public CheckedJob save(CheckedJob checkedJob) {
        Optional<CheckedJob> existingCheckedJob = checkedJobRepository.findByTenantIdAndJobId(checkedJob.getTenant().getId(), checkedJob.getJob().getId());
        if (existingCheckedJob.isPresent()) {
            return existingCheckedJob.get();
        }
        checkedJob.setLocalDate(LocalDate.now());
        return checkedJobRepository.save(checkedJob);
    }

    public void deleteById(Long id) {
        checkedJobRepository.deleteById(id);
    }

    public List<CheckedJob> findByTenantId(Long tenantId) {
        return checkedJobRepository.findByTenantId(tenantId);
    }

    public List<CheckedJob> findByJobId(Long jobId) {
        return checkedJobRepository.findByJobId(jobId);
    }

    public Optional<CheckedJob> findByTenantIdAndJobId(Long tenantId, Long jobId) {
        return checkedJobRepository.findByTenantIdAndJobId(tenantId, jobId);
    }

    public List<CheckedJob> findByTenantIdAndLocalDate(long id, LocalDate localDate){
        return checkedJobRepository.findByTenantIdAndLocalDate(id, localDate);
    }

    public boolean isJobCheckedAndDuplicated(Long tenantId, Job job) {
        List<CheckedJob> checkedJobs = checkedJobRepository.findByTenantIdAndJobTitleAndJobCompany(tenantId, job.getTitle(), job.getCompany());
        return !checkedJobs.isEmpty();
    }


}
