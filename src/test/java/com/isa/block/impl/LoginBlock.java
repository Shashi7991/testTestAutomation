package com.isa.block.impl;

import com.isa.block.Block;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
public class LoginBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private WebDriver driver;

    @Value("${test.webdriver.login.username}")
    private String username;
    @Value("${test.webdriver.login.password}")
    private String password;

    @Override
    public void process() {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
        switchToPopupWindow();
        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username_txt")));
        usernameField.sendKeys(username);
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_password")));
        passwordField.click();
        passwordField.sendKeys(password);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin")));
        loginButton.click();
        log.info("Login Success");
    }

    private void switchToPopupWindow() {
        String mainWindowHandle = driver.getWindowHandle();
        Set<String> allWindowHandles = driver.getWindowHandles();
        for (String windowHandle : allWindowHandles) {
            if (!windowHandle.equals(mainWindowHandle)) {
                driver.switchTo().window(windowHandle);
                if (driver.getTitle().equals("::Fly Jinnah-Google Chrome")) {
                    break;
                }
            }
        }
    }
}
