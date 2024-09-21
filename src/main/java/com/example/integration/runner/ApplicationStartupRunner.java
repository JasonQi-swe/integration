package com.example.integration.runner;

import com.example.integration.scheduler.JobScheduler;
import com.example.integration.service.GoogleSheetsService;
import com.example.integration.service.TenantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ApplicationStartupRunner implements CommandLineRunner {

    @Autowired
    private JobScheduler jobScheduler;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private GoogleSheetsService googleSheetsService;

    @Override
    public void run(String... args) throws Exception {
//        List<Long> list = new ArrayList<>();
//        list.add(4L);
//        jobScheduler.runJobNow(list);
        //jobScheduler.testNow();

        //createReport();
    }

    private void createReport() throws GeneralSecurityException, IOException {
        googleSheetsService.createReport(1L);
        log.info("Finished createReport()");
    }

}
