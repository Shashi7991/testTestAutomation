package com.isa;

import com.google.common.collect.Sets;
import com.isa.entity.Ancillary;
import com.isa.entity.Passenger;
import com.isa.entity.Reservation;
import com.isa.entity.ReturnSegment;
import com.isa.entity.type.BaggageType;
import com.isa.entity.type.BundleType;
import com.isa.entity.type.ConfirmType;
import com.isa.entity.type.FareType;
import com.isa.entity.type.FlexiType;
import com.isa.entity.type.FlightType;
import com.isa.entity.type.PaxType;
import com.isa.entity.type.PaymentType;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Log4j2
public class TestObjectProvider {

    public static List<Reservation> getReservations() throws IOException {
        Ancillary ancillary = Ancillary.builder()
                .baggageType(BaggageType.BAGGAGE_23)
                .mealCount(1)
                .build();
        Ancillary returnAncillary = Ancillary.builder()
                .baggageType(BaggageType.BAGGAGE_23)
                .mealCount(1)
                .build();


        List<Passenger> passengers = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Passenger passenger = Passenger.builder()
                    .paxType(PaxType.ADULT).title("Mr.")
                    .firstName("AAA").lastName("BBB")
                    .nationality("Sri Lanka").dob("24/05/1993")
                    .ancillary(ancillary)
                    .returnAncillary(returnAncillary)
                    .build();
            passengers.add(passenger);
        }

        for (int i = 0; i < 2; i++) {
            Passenger passenger = Passenger.builder()
                    .paxType(PaxType.CHILD).title("Master")
                    .firstName("BBB").lastName("CCC")
                    .nationality("Sri Lanka").dob("12/12/2000")
                    .ancillary(ancillary)
                    .returnAncillary(returnAncillary)
                    .build();
            passengers.add(passenger);
        }

        for (int i = 0; i < 2; i++) {
            Passenger passenger = Passenger.builder()
                    .paxType(PaxType.INFANT)
                    .firstName("AAA").lastName("BBB")
                    .nationality("Sri Lanka").dob("12/12/2023")
                    .build();
            passengers.add(passenger);
        }


        ReturnSegment returnSegment = ReturnSegment.builder()
                .returnDate("31/05/2024")
//                .inboundFlightNumber("9P671")
                .inboundFlightNumber("9P841")
                .bundleType(BundleType.BASIC)
                .flexiType(FlexiType.VALUE_FLEX)
                .build();

        Reservation reservation = Reservation.builder()
                .flightType(FlightType.DIRECT)
                .fareType(FareType.DEFAULT)
                .fromLocation("KHI")
                .toLocation("LHE")
                .departureDate("26/05/2024")
                .outBoundFlightNumber("9P671")
                .paymentType(PaymentType.ON_ACCOUNT)
                .bundleType(BundleType.BASIC)
                .flexiType(FlexiType.VALUE_FLEX)
                .confirmType(ConfirmType.PAY_AND_CONFIRM)
                .passengers(passengers)
                .returnSegment(returnSegment)
                .build();

        Reservation reservation2 = Reservation.builder()
                .flightType(FlightType.DIRECT)
                .fareType(FareType.DEFAULT)
                .fromLocation("KHI")
                .toLocation("LHE")
                .departureDate("28/05/2024")
                .outBoundFlightNumber("A310")
                .paymentType(PaymentType.ON_ACCOUNT)
                .confirmType(ConfirmType.PAY_AND_CONFIRM)
                .bundleType(BundleType.VALUE)
                .flexiType(FlexiType.VALUE)
                .passengers(passengers)
                .build();

        List<Reservation> reservationList = new ArrayList<>();
        reservationList.add(reservation);
        reservationList.add(reservation2);

        return reservationList;

//        return getTestCases();
    }

    private static List<Reservation> getTestCases() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("report/test.xlsx");
        Workbook readingWorkbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = readingWorkbook.getSheetAt(0);

        int numberOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
        List<Set<String>> columnData = IntStream.range(0, numberOfColumns)
                .mapToObj(i -> new HashSet<String>())
                .collect(Collectors.toList());

        List<Reservation> reservations = new ArrayList<>();

        for (Row row : sheet) {
            if (row.getRowNum() < 3) {
                continue;
            }

            if (isRowEmpty(row)) {
                Set<List<String>> result = Sets.cartesianProduct(columnData);

                reservations.addAll(generateReservations(result));

                columnData = IntStream.range(0, numberOfColumns)
                        .mapToObj(i -> new HashSet<String>())
                        .collect(Collectors.toList());
                continue;
            }

            for (Cell cell : row) {
                String value = getCellValueAsString(cell);
                if (cell.getColumnIndex() >= numberOfColumns || value.isBlank() || value.contentEquals("-")) {
                    continue;
                }
                columnData.get(cell.getColumnIndex()).add(value);
            }
        }

        readingWorkbook.close();
        fileInputStream.close();

        return reservations;
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() == -1) {
            return true;
        }
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c);
            if (cell != null && cell.getCellType() != CellType.BLANK && !isCellInMergedRegion(cell))
                return false;
        }
        return true;
    }

    private static boolean isCellInMergedRegion(Cell cell) {
        Sheet sheet = cell.getSheet();

        int numMergedRegions = sheet.getNumMergedRegions();
        for (int i = 0; i < numMergedRegions; i++) {
            CellRangeAddress mergedRegion = sheet.getMergedRegion(i);
            if (mergedRegion.isInRange(cell.getRowIndex(), cell.getColumnIndex())) {
                return true;
            }
        }
        return false;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> "UNSUPPORTED_TYPE";
        };
    }

    // TODO: Read each record according to excel file
    private static List<Reservation> generateReservations(Set<List<String>> records) {
        List<Reservation> reservations = new ArrayList<>();
        for (List<String> record : records) {
            Reservation reservation = Reservation.builder().build();
        }

        return reservations;
    }

}
