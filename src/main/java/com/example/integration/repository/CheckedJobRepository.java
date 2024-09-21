package com.example.integration.repository;

import com.example.integration.entity.Application;
import com.example.integration.entity.CheckedJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckedJobRepository extends JpaRepository<CheckedJob, Long> {
    List<CheckedJob> findByTenantId(Long tenantId);
    List<CheckedJob> findByJobId(Long jobId);
    Optional<CheckedJob> findByTenantIdAndJobId(Long tenantId, Long jobId);
    List<CheckedJob> findByTenantIdAndJobTitleAndJobCompany(Long tenantId, String title, String company);

    @Query("SELECT a FROM CheckedJob a WHERE a.tenant.id = :tenantId AND DATE(a.addedDateTime) = :date")
    List<CheckedJob> findAllByTenantIdAndLocalDate(@Param("tenantId") Long tenantId, @Param("date") LocalDate date);
}
