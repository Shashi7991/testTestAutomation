package com.isa.utility;

import com.isa.context.ReservationContext;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Log4j2
public class ExcelUtility {
    @Autowired
    private ReservationContext reservationContext;

    private Workbook workbook;

    public Row getNewRow() {
        initializeWorkbookInputStream();
        Sheet sheet = this.workbook.getSheetAt(0);
        int currentRow = sheet.getLastRowNum() + 1;
        return sheet.createRow(currentRow);
    }

    public synchronized void writeToNewExcelCell(int cellIndex, String value) {
        try {
            initializeWorkbookInputStream();
            Row dataRow = reservationContext.getCurrentRow();
            Cell cell = dataRow.createCell(cellIndex);
            cell.setCellValue(value);
            writeWorkbookOutputStream(workbook);
        } catch (IOException exception) {
            log.error("{}", exception.getMessage());
        }
    }

    private void initializeWorkbookInputStream() {
        try {
            if (workbook == null) {
                try (FileInputStream fileInputStream = new FileInputStream("report/test_results.xlsx")) {
                    workbook = new XSSFWorkbook(fileInputStream);
                }
            }
        } catch (Exception exception) {
            log.error("{}", exception.getMessage());
        }
    }

    private void writeWorkbookOutputStream(Workbook workbook) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream("report/test_results.xlsx")) {
            workbook.write(outputStream);
            log.info("Test data written to Excel file: " + "test_results.xlsx");
        }
    }

}