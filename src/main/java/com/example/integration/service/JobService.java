package com.example.integration.service;

import com.example.integration.entity.Job;
import com.example.integration.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public Optional<Job> findById(Long id) {
        return jobRepository.findById(id);
    }

    public Job save(Job job) {
        job.setAddedDate(LocalDate.now());
        return jobRepository.save(job);
    }

    public void deleteById(Long id) {
        jobRepository.deleteById(id);
    }

    public List<Job> findByCompany(String company) {
        return jobRepository.findByCompany(company);
    }

    public List<Job> findByLocation(String location) {
        return jobRepository.findByLocation(location);
    }

    public List<Job> findByType(String type) {
        return jobRepository.findByType(type);
    }

}
