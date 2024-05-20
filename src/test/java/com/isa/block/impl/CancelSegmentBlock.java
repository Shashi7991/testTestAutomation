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

import java.time.Duration;
import java.util.List;

@Component
@Log4j2
public class CancelSegmentBlock implements Block {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;

    @Override
    public void process() {
        selectCancelSegment();
        clickConfirm();
    }

    private void selectCancelSegment(){
        WebElement btnRemoveSegment = wait.until(ExpectedConditions.elementToBeClickable(By.id("removeRow_1")));
        btnRemoveSegment.click();
    }

    private void clickConfirm(){

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("divLoadBg")));

        WebElement btnBooking = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("btnBook")));
        btnBooking.click();
        log.info("Clicked cancel confirm button");
    }
}