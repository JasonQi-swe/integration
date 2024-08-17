package com.example.integration.repository;

import com.example.integration.entity.CheckedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckedJobRepository extends JpaRepository<CheckedJob, Long> {
    List<CheckedJob> findByTenantId(Long tenantId);
    List<CheckedJob> findByJobId(Long jobId);
    Optional<CheckedJob> findByTenantIdAndJobId(Long tenantId, Long jobId);
    List<CheckedJob> findByTenantIdAndLocalDate(long id, LocalDate localDate);

    List<CheckedJob> findByTenantIdAndJobTitleAndJobCompany(Long tenantId, String title, String company);
}
