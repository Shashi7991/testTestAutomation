package com.isa.block.impl;

import com.isa.block.Block;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class LogoutBlock implements Block {

    @Autowired
    private WebDriverWait wait;

    @Autowired
    private WebDriver driver;

    @Override
    public void process() {
        driver.switchTo().parentFrame();
        driver.switchTo().frame(0);

        WebElement closeButton = wait.until(ExpectedConditions.elementToBeClickable(By.className("winClose")));
        closeButton.click();

        Alert alert = driver.switchTo().alert();
        alert.accept();

        log.info("Log out successful");
    }

}
