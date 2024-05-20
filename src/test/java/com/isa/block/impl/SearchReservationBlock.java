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

import static com.isa.block.impl.ItineraryBlock.pnrNo;

@Component
@Log4j2
public class SearchReservationBlock implements Block {
    @Autowired
    private WebDriver driver;
    @Autowired
    private WebDriverWait wait;
    @Override
    public void process() {
        clickSearchReservation();
        System.out.println("Pnr no is: "+pnrNo);
        searchPNR(pnrNo);
    }

    private void clickSearchReservation(){
        driver.switchTo().parentFrame();
        driver.switchTo().frame(0);
        WebElement findReservaion = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div[3]/nobr")));
        findReservaion.click();
        driver.switchTo().parentFrame();
        driver.switchTo().frame(1);
    }

    private void searchPNR(String PNR){
        WebElement pnrElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("pnr")));
        pnrElement.sendKeys(PNR);
        WebElement searchButton = driver.findElement((By.id("btnSearch")));
        searchButton.click();
        WebElement searchResult = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1_2_res")));
        searchResult.click();
        System.out.println("Search Success");
    }
}
