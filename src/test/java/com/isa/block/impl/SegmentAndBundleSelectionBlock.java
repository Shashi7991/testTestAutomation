package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.entity.Reservation;
import com.isa.entity.type.BundleType;
import com.isa.entity.type.FareType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class SegmentAndBundleSelectionBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private WebDriver driver;
    @Autowired
    private ReservationContext reservationContext;

    private Reservation reservation;
    private final int oneWayFlight = 1;
    private final int returnFlight = 3;
    private final String xpathOfFlightTable = "//html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[%d]/div[2]/table/tbody/tr/td[1]/div/div[3]/div[3]/table/tbody/tr";
    private String flightId;       //  /html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[3!!]/div[2]/table/tbody/tr/td[1]/div/div[3]/div[3]/table/tbody/tr
    private String dynamicXpath = "/html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[1]/div[2]/table/tbody/tr/td[1]/div/div[3]/div[3]/table/tbody/tr[%d]/td[7]";
    private String basicBundlePath = "/html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[%d]/div[2]/table/tbody/tr/td[3]/div/div/div[3]/div[3]/table/tbody/tr[1]/td[1]/input";
    private String valueBundlePath = "/html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[%d]/div[2]/table/tbody/tr/td[3]/div/div/div[3]/div[3]/table/tbody/tr[2]/td[1]/input";

    @Override
    public void process() {
        reservation = reservationContext.getCurrentReservation();
        selectFlight();
        fareQuote();
        bundleSelection();
        fareQuote();

        Reservation reservation = reservationContext.getCurrentReservation();

        if (reservation.getFareType().equals(FareType.DEFAULT)) {
            clickBookButton();
            handleConfirmationDialog();
        }
    }

    private void selectFlight() {
        if (reservation.getReturnSegment() == null) {
            chooseFlight(oneWayFlight);
            log.info("One way flight selected");
        } else {
            chooseFlight(returnFlight);
            log.info("Return flight selected");
        }
    }

    private void chooseFlight(int journeyType) {

        for (int j = 1; j <= journeyType; j += 2) {
            List<WebElement> rows = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(String.format(xpathOfFlightTable, j))));
            log.info("Number of elements under flight segment : " + rows.size());
            WebElement row;
            log.info("Choosing div" + j);

            if (j == 1) {
                flightId = reservation.getOutBoundFlightNumber();
            } else {
                flightId = reservation.getReturnSegment().getInboundFlightNumber();
            }

            for (int i = 0; i < rows.size(); i++) {
                row = rows.get(i);
                String dynamicPath = String.format(dynamicXpath, i + 1);
                System.out.println("dynamic path : " + dynamicPath);
                WebElement isFull = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicPath)));
                log.info(i + "st Flight is full ? " + isFull.getText());

                if (row.getText().contains(flightId)) {
                    log.info("Flight founded : " + flightId);

                    if (!isFull.getText().contains("Flight Full")) {
                        //Add break statement to select flight
                        log.info("Flight selected");
                        isFull.click();
                    }
                } else {

                    if (!isFull.getText().contains("Flight Full")) {
                        //Add break statement to select flight
                        log.info("Flight selected");
                        isFull.click();
                    }
                }
            }
        }
    }

    private void bundleSelection() {
        int looper;
        BundleType bundleType;
        if (reservation.getReturnSegment() == null) {
            looper = oneWayFlight;
        } else {
            looper = returnFlight;
        }
        for (int i = 1; i <= looper; i += 2) {
            WebElement radioButton;

            if (i == 1) {
                bundleType = reservation.getBundleType();
            } else {
                bundleType = reservation.getReturnSegment().getBundleType();
            }

            switch (bundleType) {
                case BASIC:
                    radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(basicBundlePath, i))));
                    radioButton.click();
                    break;
                case VALUE:
                    radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(String.format(valueBundlePath, i))));
                    radioButton.click();
                    break;
            }
        }
    }

    private void fareQuote() {
        WebElement fareQuoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnFQ")));
        fareQuoteButton.click();
    }

    private void clickBookButton() {
        WebElement makeReservationButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnBook")));
        makeReservationButton.click();
    }

    private void handleConfirmationDialog() {
        driver.switchTo().alert().accept();
    }
}
