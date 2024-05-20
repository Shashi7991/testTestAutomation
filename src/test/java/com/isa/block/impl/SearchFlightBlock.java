package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.entity.Passenger;
import com.isa.entity.Reservation;
import com.isa.entity.type.PaxType;
import com.isa.utility.ExcelUtility;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.isa.constant.ExcelColumns.PAX_COLUMN_INDEX;


@Component
@Log4j2
public class SearchFlightBlock implements Block {

    @Autowired
    private WebDriverWait wait;
    @Autowired
    private WebDriver driver;
    @Autowired
    private ExcelUtility excelUtility;
    @Autowired
    private ReservationContext reservationContext;

    @Override
    public void process() {
        clickMakeReservationTab();
        Reservation reservation = reservationContext.getCurrentReservation();
        log.info(reservation.toString());
        setDepartureLocation(reservation.getFromLocation());

        if (reservation.getReturnSegment() != null) {
            setReturnDate(reservation.getReturnSegment().getReturnDate());
        }
        handlePassengerCount(reservation.getPassengers());
        setArrivalLocation(reservation.getToLocation());
        setDepartureDate(reservation.getDepartureDate());
        if (reservation.getReturnSegment() != null) {
            setReturnDate(reservation.getReturnSegment().getReturnDate());
        }
        clickSearchButton();
        WebElement paxSummary = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("divFQPerPaxSummary")));
        excelUtility.writeToNewExcelCell(PAX_COLUMN_INDEX, paxSummary.getText());
    }

    private void clickMakeReservationTab() {
        driver.switchTo().parentFrame();
        driver.switchTo().frame(0);
        WebElement makeReservationLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nobr[contains(.,'Make Reservation')]")));
        makeReservationLink.click();
        log.info("Make reservation button clicked");
        driver.switchTo().parentFrame();
        driver.switchTo().frame(1);
    }

    private void handlePassengerCount(List<Passenger> passengers) {
        int adultCount = 0;
        int childCount = 0;
        int infantCount = 0;
        for (Passenger passenger : passengers) {
            if (passenger.getPaxType().equals(PaxType.ADULT)) {
                adultCount++;
            }
            if (passenger.getPaxType().equals(PaxType.CHILD)) {
                childCount++;
            }
            if (passenger.getPaxType().equals(PaxType.INFANT)) {
                infantCount++;
            }
        }
        if (adultCount > 0)
            setAdultCount(adultCount);
        if (childCount > 0)
            setChildCount(childCount);
        if (infantCount > 0)
            setInfantCount(infantCount);
    }

    private void setAdultCount(int count) {
        WebElement adultCountField = wait.until(ExpectedConditions.elementToBeClickable(By.id("adultCount")));
        adultCountField.clear();
        adultCountField.sendKeys(String.valueOf(count));
    }

    private void setChildCount(int count) {
        WebElement childCountField = wait.until(ExpectedConditions.elementToBeClickable(By.id("childCount")));
        childCountField.clear();
        childCountField.sendKeys(String.valueOf(count));
    }

    private void setInfantCount(int count) {
        WebElement infantCountField = wait.until(ExpectedConditions.elementToBeClickable(By.id("infantCount")));
        infantCountField.clear();
        infantCountField.sendKeys(String.valueOf(count));
    }

    private void setDepartureLocation(String departureLocation) {
        WebElement fromAirportField = wait.until(ExpectedConditions.elementToBeClickable(By.id("fAirport")));
        fromAirportField.sendKeys(departureLocation);

        WebElement fromAirportDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-combo-selected")));
        fromAirportDropdown.click();
    }

    private void setArrivalLocation(String arrivalLocation) {
        WebElement toAirportField = wait.until(ExpectedConditions.elementToBeClickable(By.id("tAirport")));
        toAirportField.sendKeys(arrivalLocation);

        WebElement toAirportDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-combo-selected")));
        toAirportDropdown.click();

    }

    private void setDepartureDate(String date) {
        WebElement departureDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("departureDate")));
        departureDate.clear();
        departureDate.sendKeys(date);
    }

    private void setReturnDate(String date) {
        WebElement returnDate = wait.until(ExpectedConditions.elementToBeClickable(By.id("returnDate")));
        returnDate.clear();
        returnDate.sendKeys(date);
    }


    private void clickSearchButton() {
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSearch")));
        searchButton.click();
    }

}
