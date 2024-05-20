package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.entity.Passenger;
import com.isa.entity.Reservation;
import com.isa.entity.type.PaxType;
import com.isa.entity.type.PaymentType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Log4j2
public class PassengerDetailsBlock implements Block {

    @Autowired
    private WebDriver driver;

    @Autowired
    private WebDriverWait wait;

    @Autowired
    private ReservationContext reservationContext;

    @Override
    public void process() {
        Reservation reservation = reservationContext.getCurrentReservation();
        setPassengerData(reservation.getPassengers());
        continueFromPassengerTab(reservation.getPaymentType());
    }

    private void setPassengerData(List<Passenger> passengers) {
        WebElement btnSetData = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSetData")));
        btnSetData.click();

        Map<Boolean, List<Passenger>> partitionedPassengers = passengers.stream()
                .collect(Collectors.partitioningBy(passenger -> passenger.getPaxType().equals(PaxType.INFANT)));
        List<Passenger> adultAndChildPassengers = partitionedPassengers.get(false);
        List<Passenger> infantPassengers = partitionedPassengers.get(true);

        WebElement tableAdultAndChild = driver.findElement(By.id("tblPaxAdt"));
        List<WebElement> adultAndChildPaxRows = tableAdultAndChild.findElements(By.cssSelector("tbody tr"));
        for (int i = 0; i < adultAndChildPaxRows.size(); i++) {
            Passenger passenger = adultAndChildPassengers.get(i);

            int index = i + 1;

            WebElement titleElement = driver.findElement(By.id(index + "_displayAdultTitle"));
            Select titleSelect = new Select(titleElement);
            titleSelect.selectByVisibleText(passenger.getTitle());

            WebElement firstNameElement = driver.findElement(By.id(index + "_displayAdultFirstName"));
            firstNameElement.clear();
            firstNameElement.sendKeys(passenger.getFirstName());

            WebElement lastNameElement = driver.findElement(By.id(index + "_displayAdultLastName"));
            lastNameElement.clear();
            lastNameElement.sendKeys(passenger.getLastName());

            WebElement nationalityElement = driver.findElement(By.id(index + "_displayAdultNationality"));
            Select nationalitySelect = new Select(nationalityElement);
            nationalitySelect.selectByVisibleText(passenger.getNationality());
        }

        WebElement tableInfants = driver.findElement(By.id("tblPaxInf"));
        List<WebElement> infantPaxRows = tableInfants.findElements(By.cssSelector("tbody tr"));
        for (int i = 0; i < infantPaxRows.size(); i++) {
            Passenger passenger = infantPassengers.get(i);

            int index = i + 1;

            WebElement firstNameElement = driver.findElement(By.id(index + "_displayInfantFirstName"));
            firstNameElement.clear();
            firstNameElement.sendKeys(passenger.getFirstName());

            WebElement lastNameElement = driver.findElement(By.id(index + "_displayInfantLastName"));
            lastNameElement.clear();
            lastNameElement.sendKeys(passenger.getLastName());

            WebElement nationalityElement = driver.findElement(By.id(index + "_displayInfantNationality"));
            Select nationalitySelect = new Select(nationalityElement);
            nationalitySelect.selectByVisibleText(passenger.getNationality());

            WebElement dateOfBirthElement = driver.findElement(By.id(index + "_displayInfantDOB"));
            dateOfBirthElement.clear();
            dateOfBirthElement.sendKeys(passenger.getDob());

            WebElement whomWithTravellingElement = driver.findElement(By.id(index + "_displayInfantTravellingWith"));
            Select whomWithTravellingSelect = new Select(whomWithTravellingElement);
            whomWithTravellingSelect.selectByValue(String.valueOf(index));
        }
    }

    private void continueFromPassengerTab(PaymentType paymentType) {
        WebElement passengerTabAction;
        if (paymentType.equals(PaymentType.ON_HOLD)) {
            passengerTabAction = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPHold")));
            passengerTabAction.click();
        } else {
            passengerTabAction = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnContinue")));
            passengerTabAction.click();
        }
    }
}
