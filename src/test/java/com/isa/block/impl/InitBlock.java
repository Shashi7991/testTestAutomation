package com.isa.block.impl;

import com.isa.block.Block;
import com.isa.context.ReservationContext;
import com.isa.utility.ExcelUtility;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class InitBlock implements Block {
    @Autowired
    WebDriver webDriver;
    @Autowired
    ExcelUtility excelUtility;
    @Autowired
    ReservationContext reservationContext;

    @Override
    public void process() {
        excelUtility.writeToNewExcelCell(0, reservationContext.getCurrentReservation().toString());
    }

}
