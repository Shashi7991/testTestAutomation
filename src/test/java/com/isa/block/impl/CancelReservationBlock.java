package com.isa.block.impl;

import com.isa.block.Block;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Log4j2
public class CancelReservationBlock implements Block {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;

    @Override
    public void process(){
        cancelReservation();
    }

    private void cancelReservation(){
        driver.switchTo().frame(0);
        WebElement btnCancel = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCancel")));
        btnCancel.click();

        switchToPopupWindow(driver);
        WebElement btnConfirm = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnConfirm")));
        btnConfirm.click();
        log.info("Cancelled reservation");
    }

    private void switchToPopupWindow(WebDriver driver){
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
