package com.isa.block.impl;

import com.isa.block.Block;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class FinalizeBlock implements Block {
    @Autowired
    WebDriver webDriver;

    @Override
    public void process() {
        log.info("Ended");
    }

}
