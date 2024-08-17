package com.example.integration.webdriver;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SeleniumTest {
    public static void main(String[] args) {
        // WebDriverManager setup is handled internally
        WebDriver driver = new ChromeDriver(new ChromeOptions());
        driver.get("https://www.linkedin.com/login?fromSignIn=true");
        //driver.get("https://www.linkedin.com/jobs/view/3981803968");


        driver.findElement(By.id("username")).sendKeys("7example@gmail.com");
        driver.findElement(By.id("password")).sendKeys("");


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement signInButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn__primary--large.from__button--floating[data-litms-control-urn='login-submit']")));
        signInButton.click();


        driver.get("https://www.linkedin.com/jobs/view/3981803968");
        //driver.findElement(By.xpath("//button[contains(.,'more')]")).click();
        WebElement seeMoreButton = driver.findElement(By.xpath("//span[text()='See more']"));
        seeMoreButton.click();


        WebElement jobDetailsElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("job-details")));



        // Extract the text from the element
        String jobDetail = jobDetailsElement.getText();
        // Perform test actions
        driver.quit();
    }
}
