package com.example.integration.service;


import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class WebDriverService {

    @Value("${LINKEDIN_U}")
    private String linkedinU;

    @Value("${LINKEDIN_P}")
    private String linkedinP;

    private WebDriver driver;

    private WebDriverWait wait;

    public void initiate() {
        if (driver == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            driver = new ChromeDriver(options);

            wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            driver.get("https://www.linkedin.com/login?fromSignIn=true");
            WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
            usernameField.sendKeys(linkedinU);

            WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
            passwordField.sendKeys(linkedinP);
            WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn__primary--large.from__button--floating[data-litms-control-urn='login-submit']")));
            signInButton.click();
        }
    }

    public Map<String, String> getJobDetail(String url) {

        Map<String, String> map = new HashMap<>();
        log.info("debug: entered : {}", url);
        try {
            if (driver == null) {
                initiate();
            }

            driver.get(url);
            Thread.sleep(6000);

            int attempts = 0;
            while (attempts < 3) {
                try {
                    WebElement seeMoreButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='See more']")));
                    seeMoreButton.click();
                    break;
                } catch (TimeoutException e) {
                    log.warn("Attempt {} - Could not find 'See more' button, retrying...", attempts + 1);
                    attempts++;
                }
            }

            WebElement jobDetailsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job-details")));
            String description = jobDetailsElement.getText();

            WebElement divElement = driver.findElement(By.className("job-details-jobs-unified-top-card__job-title"));
            String title = divElement.getText();
            if (description.equals("") || description.isEmpty()) {
                log.error("description is empty for job " + url);
                throw new RuntimeException("description is empty for job " + url);
            }
            map.put("description", description);
            map.put("title", title);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to retrieve job details: " + e.getMessage());
        }
        return map;
    }


    private void close() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
