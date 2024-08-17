package com.example.integration.repository;

import com.example.integration.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCompany(String company);
    List<Job> findByLocation(String location);
    List<Job> findByType(String type);
}
