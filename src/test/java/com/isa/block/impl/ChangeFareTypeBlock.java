package com.isa.block.impl;

import com.isa.block.Block;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class ChangeFareTypeBlock implements Block {

    @Autowired
    private WebDriverWait wait;

    @Autowired
    private WebDriver driver;

    @Override
    public void process() {
        WebElement showFaresButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCFare")));
        showFaresButton.click();

        String winHandleBefore = driver.getWindowHandle();

        List<String> windowHandleList = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(windowHandleList.get(windowHandleList.size() - 1));

        int row = 0;
        while (true) {
            String elementId = "td_" + row + "_0";
            try {
                WebElement tableElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//table[@id='tblSegemnts']")));

                WebElement segmentRecord = tableElement.findElement(By.xpath(".//td[@id='" + elementId + "']"));
                segmentRecord.click();

                WebElement showFaresSegmentButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnShowFares")));
                showFaresSegmentButton.click();

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='divAvailableFares']//table")));

                WebElement radioButton = driver.findElement(By.xpath("//input[@type='radio' and @value='5^0']"));
                radioButton.click();

                WebElement recalculateButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnReCal")));
                recalculateButton.click();

                row++;
            } catch (NoSuchElementException e) {
                log.error("No element found with ID: {}. Breaking the loop.", elementId);

                int windowsBeforeClick = driver.getWindowHandles().size();

                WebElement confirmSegmentFaresBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnConfim")));
                confirmSegmentFaresBtn.click();

                wait.until(ExpectedConditions.numberOfWindowsToBe(windowsBeforeClick - 1));

                driver.switchTo().window(winHandleBefore);

                driver.switchTo().parentFrame();
                driver.switchTo().frame(1);
                WebElement makeReservationButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnBook")));
                makeReservationButton.click();

                try {
                    WebDriverWait limitedWait = new WebDriverWait(driver, Duration.ofSeconds(3)); // Since sometimes an alert popup occurs
                    Alert alert = limitedWait.until(ExpectedConditions.alertIsPresent());
                    alert.accept();
                } catch (TimeoutException ignored) {
                }

                break;
            }
        }
    }

}
