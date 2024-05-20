package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.entity.Ancillary;
import com.isa.entity.Passenger;
import com.isa.entity.Reservation;
import com.isa.entity.type.BaggageType;
import com.isa.entity.type.FlexiType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class AncillaryBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    private ReservationContext reservationContext;
    @Autowired
    WebDriver driver;
    private boolean isOneWayReservationProcessing = false;
    private boolean isReturnReservationProcessing = false;

    @Override
    public void process() {
        Reservation reservation = reservationContext.getCurrentReservation();
        List<Ancillary> ancillaryList = new ArrayList<>();
        for (Passenger p : reservation.getPassengers()) {
            ancillaryList.add(p.getAncillary());
        }
        List<Ancillary> returnAncillaryList = new ArrayList<>();
        if (reservation.getReturnSegment() != null) {
            for (Passenger p : reservation.getPassengers()) {
                returnAncillaryList.add(p.getReturnAncillary());
            }
        }

        boolean isReturnReservation = reservation.getReturnSegment() != null;

//        switch (reservationContext.getCurrentReservation().getBundleType()) {
//            case VALUE:
//                configureAnciForValueBundle();
//                break;
//        }
//        clickContinueFromAncillaryTab();
//        clickContinueFromAncillaryOk();
        if (isReturnReservation) {
            isOneWayReservationProcessing = true;
        }
        switchToBaggageTab();
        setBaggages(ancillaryList);
        switchToMealsTab();
        setMeals(ancillaryList);
        switchToFlexiTab();
        setFlexi(reservation.getFlexiType());
        isOneWayReservationProcessing = false;

        if (isReturnReservation) {
            this.isReturnReservationProcessing = true;
            switchToBaggageTab();
            setBaggages(returnAncillaryList);
            switchToMealsTab();
            setMeals(returnAncillaryList);
            switchToFlexiTab();
            setFlexi(reservation.getReturnSegment().getFlexiType());
        }
        isReturnReservationProcessing = false;

        clickContinueFromAncillaryTab();
        clickContinueFromAncillaryOk();
    }

    private void setBaggages(List<Ancillary> ancillaryList) {

        List<WebElement> segments = getSegments();
        WebElement btnAnciBaggageApply = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciBaggageApply")));

        int i = 0;
        int segmentSize = segments.size();
        if (isOneWayReservationProcessing) {
            segmentSize = segmentSize / 2;
        }
        if (isReturnReservationProcessing) {
            log.info("isReturnReservationProcessing");
            i += segmentSize / 2;
        }
        for (; i < segmentSize; i = i + 2) {
            log.info(i + "i value");
            log.info(segmentSize + "Segment size");
            WebElement segment = segments.get(i);
            segment.click();
            int anciIndex = 0;
            WebElement baggagePax = wait.until(ExpectedConditions.elementToBeClickable(By.id("tbdyAnciBaggagePax")));
            List<WebElement> paxRows = baggagePax.findElements(By.tagName("tr"));
            for (int j = 0; j < paxRows.size(); j = j + 2) {
                Ancillary ancillary = ancillaryList.get(anciIndex);
                WebElement paxRow = paxRows.get(j);
                paxRow.click();

                WebElement baggageSelection = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tblAnciBaggage']/tbody")));
                List<WebElement> baggageRows = baggageSelection.findElements(By.tagName("tr"));
                for (int k = 0; k < baggageRows.size(); k++) {
                    BaggageType baggageType = ancillary.getBaggageType();
                    log.info(baggageType.getValue() + " Input Baggage");
                    WebElement baggageRow = baggageRows.get(k);

                    List<WebElement> baggageTypeColumn = baggageRow.findElements(By.tagName("td"));
                    WebElement baggageTypeElement = baggageTypeColumn.get(0);
                    log.info(baggageTypeElement.getText() + " Current type");

                    if (baggageTypeElement.getText().equals(baggageType.getValue())) {
                        log.info("Found Proceeding");
                        baggageRow.click();
                        btnAnciBaggageApply.click();
                        break;
                    }
                }
                anciIndex++;
            }

        }

    }

    private void setMeals(List<Ancillary> ancillaryList) {

        List<WebElement> segments = getSegments();
        WebElement btnAnciMealApply = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciMealApply")));
        int i = 0;
        int segmentSize = segments.size();
        if (isOneWayReservationProcessing) {
            segmentSize = segmentSize / 2;
        }
        if (isReturnReservationProcessing) {
            log.info("isReturnReservationProcessing");
            i += segmentSize / 2;
        }
        for (; i < segmentSize; i = i + 2) {
            log.info(i + "i value");
            log.info(segmentSize + "Segment size");
            WebElement segment = segments.get(i);
            segment.click();

            WebElement mealPax = wait.until(ExpectedConditions.elementToBeClickable(By.id("tbdyAnciMealPax")));
            List<WebElement> paxRows = mealPax.findElements(By.tagName("tr"));
            int paxRowsSize = paxRows.size();
            log.info(paxRowsSize + " Pax rows size");

            int anciIndex = 0;
            for (int j = 0; j < paxRowsSize * 2; j = j + 2) {
                mealPax = wait.until(ExpectedConditions.elementToBeClickable(By.id("tbdyAnciMealPax")));
                paxRows = mealPax.findElements(By.tagName("tr"));
                log.info(paxRows.size() + " Pax rows size");
                log.info("{} j value", j);

                Ancillary ancillary = ancillaryList.get(anciIndex);
                WebElement paxRow = paxRows.get(j);
                List<WebElement> paxElements = paxRow.findElements(By.tagName("td"));
                WebElement p = paxElements.get(0).findElement(By.tagName("p"));
                log.info("{} Pax row", p.getText());
                p.click();

                WebElement MealSelection = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='tblAnciMealBundle']/tbody")));
                List<WebElement> mealsRows = MealSelection.findElements(By.tagName("tr"));

                for (int k = 0; k < mealsRows.size(); k++) {
                    WebElement mealsRow = mealsRows.get(k);
                    log.info(" Input Meal processing");
                    List<WebElement> mealElements = mealsRow.findElements(By.tagName("td"));
                    WebElement mealRow = mealElements.get(4);
                    WebElement inputElement = mealRow.findElement(By.tagName("input"));
                    inputElement.clear();
                    inputElement.sendKeys(ancillary.getMealCount().toString());
                    btnAnciMealApply.click();
                    break;
                }
                anciIndex++;
            }
        }
    }

    private void setFlexi(FlexiType flexiType) {
        log.info("Flexi Started ");
        List<WebElement> segments = getSegments();
        WebElement btnAnciFlexiApply = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnFlexiApply")));
        int i = 0;
        int segmentSize = segments.size();
        log.info(segmentSize + " Segment Size");
        if (isOneWayReservationProcessing) {
            segmentSize = segmentSize / 2;
        }
        if (isReturnReservationProcessing) {
            i += segmentSize / 2;
        }
        for (; i < segmentSize; i = i + 2) {
            log.info(i + "i value");
            log.info(segmentSize + " Segment size");
            WebElement segment = segments.get(i);
            segment.click();

            WebElement flexiSelection = wait.until(ExpectedConditions.elementToBeClickable(By.id("flexi_bundles_bg")));
            List<WebElement> flexiRows = flexiSelection.findElements(By.tagName("span"));
            log.info(flexiRows.size() + " Flexi rows size");

            for (WebElement row : flexiRows) {
                log.info(flexiType.getValue() + " Input flexi");
                List<WebElement> flexiRowElements = row.findElements(By.tagName("p"));
                WebElement flexiRowElement = flexiRowElements.get(0);
                log.info(flexiRowElement.getText() + "Present flexi");

                if (flexiType.equals(FlexiType.NO_FLEX)) {
                    log.info("No flexi applying");
                    WebElement radioButton = row.findElement(By.tagName("input"));
                    radioButton.click();
                    btnAnciFlexiApply.click();
                    break;
                }
                if (flexiRowElement.getText().equals(flexiType.getValue())) {
                    log.info("Applying flexi " + flexiRowElement.getText());
                    WebElement radioButton = row.findElement(By.tagName("input"));
                    radioButton.click();
                    btnAnciFlexiApply.click();
                    break;
                }

            }
        }
    }


    private void switchToBaggageTab() {
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                WebElement baggageTabButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"imgTab_5\"]/img")));
                baggageTabButton.click();
                break;
            } catch (ElementClickInterceptedException e) {
                log.info("Element click intercepted. Retrying attempt {}...", i + 1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private List<WebElement> getSegments() {
        WebElement segmentTableBody = wait.until(ExpectedConditions.elementToBeClickable(By.id("tbdyAnciSeg")));
        return segmentTableBody.findElements(By.tagName("tr"));
    }


    private void clickContinueFromAncillaryTab() {
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                WebElement btnAnciContinue = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciContinue")));
                btnAnciContinue.click();
                break;
            } catch (ElementClickInterceptedException e) {
                log.info("Element click intercepted. Retrying attempt {}...", i + 1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void clickContinueFromAncillaryOk() {
        WebElement btnAnciContOk = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciContOk")));
        btnAnciContOk.click();
    }

    private void switchToFlexiTab() {
        WebElement flexiTabButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[9]/img")));
        flexiTabButton.click();
    }

    private void switchToMealsTab() {
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                WebElement mealElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".meal")));
                mealElement.click();
                break;
            } catch (ElementClickInterceptedException e) {
                log.info("Element click intercepted. Retrying attempt {}...", i + 1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private void configureAnciForValueBundle() {
        int attempts = 3;
        for (int i = 0; i < attempts; i++) {
            try {
                WebElement mealElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".meal")));
                mealElement.click();
                break;
            } catch (ElementClickInterceptedException e) {
                log.info("Element click intercepted. Retrying attempt {}...", i + 1);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        WebElement mealQtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1_mealQty")));
        mealQtyElement.sendKeys("1");

        WebElement anciMealApply = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciMealApply")));
        anciMealApply.click();

        WebElement flexiTabButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[9]/img")));
        flexiTabButton.click();

        WebElement valueRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("ValueRadio")));
        valueRadioBtn.click();
    }


}
