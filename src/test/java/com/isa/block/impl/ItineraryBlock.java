package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.utility.ExcelUtility;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.isa.constant.ExcelColumns.PNR_COLUMN_INDEX;

@Component
@Log4j2
public class ItineraryBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private ExcelUtility excelUtility;
    public static String pnrNo;

    @Override
    public void process() {
        goToReservationPage();
    }

    private void goToReservationPage() {
        WebElement pnrLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("itineraryPNRLink")));
        pnrLink.click();
        excelUtility.writeToNewExcelCell(2, pnrLink.getText());

    }

}
