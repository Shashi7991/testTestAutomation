package com.isa.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Duration;

@Configuration
@Log4j2
public class WebDriverConfig {

    @Value("${test.webdriver.chrome-driver-path}")
    private String chromeDriverPath;

    @Value("${test.webdriver.base-url}")
    private String baseUrl;

    private WebDriver driver;

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public WebDriver getWebDriver() {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        driver = new ChromeDriver();
        driver.get(baseUrl);
        driver.manage().window().maximize();
        return driver;
    }

    @PreDestroy
    public void quitWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Bean
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriverWait getWebDriverWait(WebDriver webDriver) {
        return new WebDriverWait(webDriver, Duration.ofMinutes(1L));
    }

}
