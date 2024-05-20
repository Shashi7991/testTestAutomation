package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.entity.Reservation;
import com.isa.entity.type.ConfirmType;
import com.isa.entity.type.PaymentType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Log4j2
public class PaymentBlock implements Block {
    @Autowired
    private WebDriverWait wait;
    @Autowired
    ReservationContext reservationContext;

    @Override
    public void process() {
        Reservation reservation = reservationContext.getCurrentReservation();
        PaymentType paymentType = reservation.getPaymentType();
        ConfirmType confirmType = reservation.getConfirmType();
        if (paymentType == null) {
            log.error("Payment type is null");
            throw new RuntimeException("Payment type is null");
        }
        if (confirmType == null) {
            log.error("Confirm type is null");
            throw new RuntimeException("Confirm type is null");
        }
        log.info("Payment block");
        selectPaymentMethod(reservation.getPaymentType());
        clickContinueFromPayTab(reservation.getConfirmType());
    }

    private void clickContinueFromPayTab(ConfirmType confirmType) {
        switch (confirmType) {
            case PAY_AND_CONFIRM:
                WebElement btnPayContinue = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPayContinue")));
                btnPayContinue.click();
                break;
            case FORCE_CONFIRM:
                WebElement chkAllPax = wait.until(ExpectedConditions.elementToBeClickable(By.id("chkAllPax")));
                chkAllPax.click();
                WebElement btnForceConfirm = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPayForce")));
                btnForceConfirm.click();
                break;
        }

    }

    private void selectPaymentMethod(PaymentType paymentType) {
        WebElement paymentMethodContainer = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"payOptions\"]/td/table/tbody/tr[4]/td/table/tbody")));
        List<WebElement> paymentMethods = paymentMethodContainer.findElements(By.tagName("tr"));

        for (WebElement paymentMethod : paymentMethods) {
            WebElement paymentMethodType = paymentMethod.findElement(By.tagName("label"));
            WebElement paymentMethodRadio = paymentMethod.findElement(By.tagName("input"));
            String paymentMethodTypeName = paymentMethodType.getText();
            if (paymentMethodTypeName.equals(paymentType.getValue())) {
                paymentMethodRadio.click();
                break;
            }

        }
    }
}
