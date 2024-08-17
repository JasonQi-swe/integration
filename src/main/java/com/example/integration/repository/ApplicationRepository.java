package com.example.integration.repository;

import com.example.integration.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByTenantId(Long tenantId);

    List<Application> findAllByTenantIdAndLocalDate(Long tenantId, LocalDate date);

    List<Application> findAllByTenantIdAndJobId(Long tenantId, Long jobId);

    @Query("SELECT a FROM Application a WHERE a.job.title = :jobTitle AND a.job.company = :jobCompany AND a.tenant.id = :tenantId")
    Optional<Application> findDuplicateApplication(@Param("jobTitle") String jobTitle, @Param("jobCompany") String jobCompany, @Param("tenantId") Long tenantId);
}
