package com.example.integration.service;

import com.example.integration.entity.Tenant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private TenantService tenantService;

    public void runTask(long id) throws GeneralSecurityException, IOException {
        Optional<Tenant> t = tenantService.findById(id);
        if(t.isPresent()) {
            Tenant tenant = t.get();
            integrationService.processJobs(tenant);
        }else{
            log.warn("Could not find the tenant with id: {}", id);
        }
    }

    public void runTask(long id, List<String> jobIds) throws IOException {
        Optional<Tenant> t = tenantService.findById(id);
        if(t.isPresent()) {
            Tenant tenant = t.get();
            integrationService.processJobIds(tenant, jobIds);
        }else{
            log.warn("Could not find the tenant with id: {}", id);
        }
    }
}
