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

@Component
@Log4j2
public class ModifyReservationBlock implements Block {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;

    @Override
    public void process() {
        selectModifyReservationBlock();
    }

    private void selectModifyReservationBlock(){
        driver.switchTo().frame(0);
        System.out.println("Select modify reservation block");
        WebElement btnModifyReservation = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnModifyRes")));
        btnModifyReservation.click();
    }
}
