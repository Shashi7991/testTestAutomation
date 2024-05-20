//package com.isa;
//
//import com.isa.config.ExcelConfig;
//import com.isa.config.WebDriverConfig;
//import lombok.extern.log4j.Log4j2;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.Select;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Set;
//
//
//@SpringBootTest(classes = {WebDriverConfig.class, ExcelConfig.class})
//@Log4j2
//class XBETest {
//
//    @Autowired
//    private WebDriver driver;
//    @Autowired
//    private WebDriverWait wait;
//    @Autowired
//    private Workbook workbook;
//
//    @AfterEach
//    public void saveToExcel() throws IOException {
//        try (FileOutputStream outputStream = new FileOutputStream("report/test_results.xlsx")) {
//            workbook.write(outputStream);
//            log.info("Test data written to Excel file: " + "test_results.xlsx");
//        }
//    }
//
//    private void writeToExcel(Row dataRow, int cellIndex, String value) {
//        Cell cell = dataRow.createCell(cellIndex);
//        cell.setCellValue(value);
//    }
//
//    @Test()
//    @Order(1)
//    void BasicBundleSinglePaxReservation() {
//        Sheet sheet = workbook.getSheetAt(0);
//        int currentRow = sheet.getLastRowNum() + 1;
//        Row dataRow = sheet.createRow(currentRow);
//        writeToExcel(dataRow, 0, "Basic Bundle Single Pax");
//
//        loginToTheSystem(driver, wait);
//        clickMakeReservation(wait, driver);
//        setDepartureLocation(wait, "khi");
//        setArrivalLocation(wait, "lhe");
//        setDepartureDate(wait, "22");
//        clickSearchButton(wait);
//        selectFlight(wait);
//        bundleSelection(wait, "B");
//        fareQuote(wait);
//
//        WebElement paxSummary = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("divFQPerPaxSummary")));
//        writeToExcel(dataRow, 1, paxSummary.getText());
//
//        log.info(paxSummary.getText());
//        clickBookButton(wait);
//        handleConfirmationDialog(driver);
//        setPassengerData(wait);
//        clickContinueFromPassengerTab(wait);
//        clickContinueFromAnciTab(wait);
//        clickContinueFromAnciOk(wait);
//        clickContinueFromPayTab(wait);
//
//        WebElement pnrLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("itineraryPNRLink")));
//        writeToExcel(dataRow, 2, pnrLink.getText());
//        log.info(pnrLink.getText());
//
//
//    }
//
//    @Test
//    @Order(2)
//    void ValueBundleSinglePaxReservation() {
//        Sheet sheet = workbook.getSheetAt(0);
//        int currentRow = sheet.getLastRowNum() + 1;
//        Row dataRow = sheet.createRow(currentRow);
//        writeToExcel(dataRow, 0, "Value Bundle Single Pax");
//        loginToTheSystem(driver, wait);
//        clickMakeReservation(wait, driver);
//        setDepartureLocation(wait, "khi");
//        setArrivalLocation(wait, "lhe");
//        setDepartureDate(wait, "22");
//        clickSearchButton(wait);
//        selectFlight(wait);
//        bundleSelection(wait, "V");
//        fareQuote(wait);
//
//        WebElement paxSummary = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("divFQPerPaxSummary")));
//        writeToExcel(dataRow, 1, paxSummary.getText());
//
//        clickBookButton(wait);
//        handleConfirmationDialog(driver);
//        setPassengerData(wait);
//        clickContinueFromPassengerTab(wait);
//        configureAnciForValueBundle(wait);
//        clickContinueFromAnciTab(wait);
//        clickContinueFromAnciOk(wait);
//        clickContinueFromPayTab(wait);
//        WebElement pnrLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("itineraryPNRLink")));
//        writeToExcel(dataRow, 2, pnrLink.getText());
//        log.info(pnrLink.getText());
//    }
//
//    private static void switchToPopupWindow(WebDriver driver) {
//        String mainWindowHandle = driver.getWindowHandle();
//        Set<String> allWindowHandles = driver.getWindowHandles();
//
//        for (String windowHandle : allWindowHandles) {
//            if (!windowHandle.equals(mainWindowHandle)) {
//                driver.switchTo().window(windowHandle);
//                if (driver.getTitle().equals("::Fly Jinnah-Google Chrome")) {
//                    break;
//                }
//            }
//        }
//    }
//
//    private static void loginToTheSystem(WebDriver driver, WebDriverWait wait) {
//        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));
//        switchToPopupWindow(driver);
//        WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.id("username_txt")));
//        usernameField.sendKeys("system");
//        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(By.id("j_password")));
//        passwordField.click();
//        passwordField.sendKeys("passis@123");
//        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnLogin")));
//        loginButton.click();
//        log.info("Login Success");
//
//    }
//
//    private static void setDepartureDate(WebDriverWait wait, String date) {
//        WebElement datePickerIcon = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("tr:nth-child(2) .ui-datepicker-trigger")));
//        datePickerIcon.click();
//        WebElement departureDate = wait.until(ExpectedConditions.elementToBeClickable(By.linkText(date)));
//        departureDate.click();
//    }
//
//    private static void setReturnDate(WebDriverWait wait) {
//
//    }
//
//    private static void setDepartureLocation(WebDriverWait wait, String departureLocation) {
//        WebElement fromAirportField = wait.until(ExpectedConditions.elementToBeClickable(By.id("fAirport")));
//        fromAirportField.sendKeys(departureLocation);
//
//        // Select the first suggestion from the 'From' airport dropdown
//        WebElement fromAirportDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[9]/div/div")));
//        fromAirportDropdown.click();
//    }
//
//    private static void setArrivalLocation(WebDriverWait wait, String arrivalLocation) {
//
//        // Wait until the 'To' airport field is visible and clickable
//        WebElement toAirportField = wait.until(ExpectedConditions.elementToBeClickable(By.id("tAirport")));
//        toAirportField.sendKeys(arrivalLocation);
//
//        // Select the first suggestion from the 'To' airport dropdown
//        WebElement toAirportDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".x-combo-selected")));
//        toAirportDropdown.click();
//
//    }
//
//    private static void clickSearchButton(WebDriverWait wait) {
//        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSearch")));
//        searchButton.click();
//    }
//
//    private static void selectFlight(WebDriverWait wait) {
//        // Select a flight (assuming there's an available flight)
//        WebElement flightSelection = wait.until(ExpectedConditions.elementToBeClickable(By.id("1_0")));
//        flightSelection.click();
//    }
//
//    private static void bundleSelection(WebDriverWait wait, String bundle) {
//        WebElement radioButton;
//        switch (bundle) {
//            case "B":
//                radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[1]/div[2]/table/tbody/tr/td[3]/div/div/div[3]/div[3]/table/tbody/tr[1]/td[1]/input")));
//                radioButton.click();
//                break;
//            case "V":
//                radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[1]/div[2]/div[1]/table/tbody/tr[1]/td/div[3]/div[3]/div[1]/div[1]/div[2]/table/tbody/tr/td[3]/div/div/div[3]/div[3]/table/tbody/tr[2]/td[1]/input")));
//                radioButton.click();
//                break;
//        }
//    }
//
//    private static void fareQuote(WebDriverWait wait) {
//        WebElement fareQuoteButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnFQ")));
//        fareQuoteButton.click();
//    }
//
//    private static void clickMakeReservation(WebDriverWait wait, WebDriver driver) {
//        driver.switchTo().frame(0);
//        WebElement makeReservationLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//nobr[contains(.,'Make Reservation')]")));
//        log.info("Menu button clicked");
//        makeReservationLink.click();
//        driver.switchTo().parentFrame();
//        driver.switchTo().frame(1);
//    }
//
//    private static void clickBookButton(WebDriverWait wait) {
//        WebElement makeReservationButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnBook")));
//        makeReservationButton.click();
//    }
//
//    private static void handleConfirmationDialog(WebDriver driver) {
//        driver.switchTo().alert().accept();
//    }
//
//    private static void setPassengerData(WebDriverWait wait) {
//        WebElement btnSetData = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSetData")));
//        btnSetData.click();
//
//        WebElement displayAdultTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("1_displayAdultTitle")));
//        displayAdultTitle.click();
//        Select dropdown = new Select(displayAdultTitle);
//        dropdown.selectByVisibleText("Mr.");
//    }
//
//    private static void clickContinueFromPassengerTab(WebDriverWait wait) {
//        WebElement btnContinue = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnContinue")));
//        btnContinue.click();
//    }
//
//    private static void clickContinueFromAnciTab(WebDriverWait wait) {
//        WebElement btnAnciContinue = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciContinue")));
//        btnAnciContinue.click();
//    }
//
//    private static void clickContinueFromAnciOk(WebDriverWait wait) {
//        WebElement btnAnciContOk = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciContOk")));
//        btnAnciContOk.click();
//    }
//
//    private static void clickContinueFromPayTab(WebDriverWait wait) {
//        WebElement btnPayContinue = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnPayContinue")));
//        btnPayContinue.click();
//    }
//
//    private static void configureAnciForValueBundle(WebDriverWait wait) {
//        WebElement mealElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".meal")));
//        mealElement.click();
//
//        WebElement mealQtyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("1_mealQty")));
//        mealQtyElement.sendKeys("1");
//
//        WebElement anciMealApply = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAnciMealApply")));
//        anciMealApply.click();
//
//        WebElement flexiTabButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[9]/img")));
//        flexiTabButton.click();
//
//        WebElement valueRadioBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("ValueRadio")));
//        valueRadioBtn.click();
//    }
//
//    private static void goToReservationPage(WebDriverWait wait) {
//        WebElement PNRButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("itineraryPNRLink")));
//        PNRButton.click();
//    }
//
//
//}