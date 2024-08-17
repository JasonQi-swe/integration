package com.example.integration.scheduler;

import com.example.integration.entity.Tenant;
import com.example.integration.service.IntegrationService;
import com.example.integration.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JobScheduler {

    @Autowired
    private TenantService tenantService;

    @Autowired
    private IntegrationService integrationService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void runJob() {
        //jobService.processJobs();
    }

    public void runJobNow(List<Long> list) throws IOException, GeneralSecurityException {
        for (Long i : list) {
            Optional<Tenant> t = tenantService.findById(i);
            if(t.isPresent()) {
                Tenant tenant = t.get();
                integrationService.processJobs(t.get());
                //jobService.createReport(tenant);
            }{
                log.warn("Could not find the tenant with id: {}", i);
            }
        }
    }

    public void testNow() {

    }
}
