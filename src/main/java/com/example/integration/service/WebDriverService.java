package com.example.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

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

    public String getJobDetail(String url) {
        log.info("debug: entered : {}", url);
        try {
            if (driver == null) {
                initiate();
            }

            driver.get(url);
            Thread.sleep(5000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='See more']")));

            WebElement seeMoreButton = driver.findElement(By.xpath("//span[text()='See more']"));
            seeMoreButton.click();

            WebElement jobDetailsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job-details")));

            String description = jobDetailsElement.getText();
            log.info("description:{}", description);
            if(description.isEmpty()){
                log.error("description is empty for job " + url);
                throw new RuntimeException("description is empty for job " + url);
            }
            return description;

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to retrieve job details: " + e.getMessage());
            return "empty";
        }
    }
}
