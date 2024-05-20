package com.isa;

import com.isa.block.Block;
import com.isa.block.impl.AncillaryBlock;
import com.isa.block.impl.CancelReservationBlock;
import com.isa.block.impl.CancelSegmentBlock;
import com.isa.block.impl.ChangeFareTypeBlock;
import com.isa.block.impl.FinalizeBlock;
import com.isa.block.impl.InitBlock;
import com.isa.block.impl.ItineraryBlock;
import com.isa.block.impl.LoginBlock;
import com.isa.block.impl.LogoutBlock;
import com.isa.block.impl.ModifyReservationBlock;
import com.isa.block.impl.PassengerDetailsBlock;
import com.isa.block.impl.PaymentBlock;
import com.isa.block.impl.SearchFlightBlock;
import com.isa.block.impl.SearchReservationBlock;
import com.isa.block.impl.SegmentAndBundleSelectionBlock;
import com.isa.context.ReservationContext;
import com.isa.entity.Reservation;
import com.isa.entity.type.FareType;
import com.isa.pipeline.TestPipeline;
import com.isa.utility.ExcelUtility;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@SpringBootTest()
@Log4j2
class Executor {

    private static Boolean loginComplete = false;
    @Autowired
    private LoginBlock loginBlock;
    @Autowired
    private LogoutBlock logoutBlock;
    @Autowired
    private SearchFlightBlock searchFlightBlock;
    @Autowired
    private InitBlock initBlock;
    @Autowired
    private ChangeFareTypeBlock changeFareTypeBlock;
    @Autowired
    private SegmentAndBundleSelectionBlock segmentAndBundleSelectionBlock;
    @Autowired
    private PassengerDetailsBlock passengerDetailsBlock;
    @Autowired
    private AncillaryBlock ancillaryBlock;
    @Autowired
    private PaymentBlock paymentBlock;
    @Autowired
    private ItineraryBlock itineraryBlock;
    @Autowired
    private FinalizeBlock finalizeBlock;
    @Autowired
    private ReservationContext reservationContext;
    @Autowired
    private CancelReservationBlock cancelReservationBlock;
    @Autowired
    private SearchReservationBlock searchReservationBlock;
    @Autowired
    private CancelSegmentBlock cancelSegmentBlock;
    @Autowired
    private ModifyReservationBlock modifyReservationBlock;
    @Autowired
    private ExcelUtility excelUtility;

    @ParameterizedTest
    @MethodSource("com.isa.TestObjectProvider#getReservations")
    void createReservation(Reservation reservation) {
        reservationContext.setCurrentReservation(reservation);
        reservationContext.setCurrentRow(excelUtility.getNewRow());

        ArrayList<Block> blockArrayList = new ArrayList<>();

        if (!loginComplete) {
            blockArrayList.add(loginBlock);
            loginComplete = true;
        }
        blockArrayList.add(initBlock);
        blockArrayList.add(searchFlightBlock);
        blockArrayList.add(segmentAndBundleSelectionBlock);

        if (reservation.getFareType().equals(FareType.SECTOR)) {
            blockArrayList.add(changeFareTypeBlock);
        }

        blockArrayList.add(passengerDetailsBlock);
        blockArrayList.add(ancillaryBlock);
        blockArrayList.add(paymentBlock);
        blockArrayList.add(itineraryBlock);
        blockArrayList.add(finalizeBlock);

        TestPipeline testPipeline = new TestPipeline(blockArrayList);
        testPipeline.execute();
    }

    @Test
    void createReservationTest() throws IOException {
        List<Reservation> reservations = TestObjectProvider.getReservations();

        for (Reservation reservation : reservations) {
            reservationContext.setCurrentRow(excelUtility.getNewRow());
            reservationContext.setCurrentReservation(reservation);

            ArrayList<Block> blockArrayList = new ArrayList<>();

            if (!loginComplete) {
                blockArrayList.add(loginBlock);
                loginComplete = true;
            }
            blockArrayList.add(initBlock);
            blockArrayList.add(searchFlightBlock);
            blockArrayList.add(segmentAndBundleSelectionBlock);

            if (reservation.getFareType().equals(FareType.SECTOR)) {
                blockArrayList.add(changeFareTypeBlock);
            }

            blockArrayList.add(passengerDetailsBlock);
            blockArrayList.add(ancillaryBlock);
            blockArrayList.add(paymentBlock);
            blockArrayList.add(itineraryBlock);
            if (reservation.isCancelReservation()) {
                log.info("Cancelling reservation ...");
                blockArrayList.add(searchReservationBlock);
                blockArrayList.add(cancelReservationBlock);
            }
            if (reservation.isCancelSegment()) {
                log.info("Cancelling segment ...");
                blockArrayList.add(searchReservationBlock);
                blockArrayList.add(modifyReservationBlock);
                blockArrayList.add(cancelSegmentBlock);
            }

            blockArrayList.add(finalizeBlock);

            TestPipeline testPipeline = new TestPipeline(blockArrayList);
            testPipeline.execute();
        }

    }

}
