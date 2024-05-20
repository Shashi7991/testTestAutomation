package com.isa.context;

import com.isa.entity.Reservation;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class ReservationContext {
    private final ThreadLocal<Reservation> currentReservation = new ThreadLocal<>();
    private final ThreadLocal<Row> currentRow = new ThreadLocal<>();

    public void setCurrentReservation(Reservation reservation) {
        currentReservation.set(reservation);
    }

    public Reservation getCurrentReservation() {
        return currentReservation.get();
    }

    public void setCurrentRow(Row row) {
        currentRow.set(row);
    }

    public Row getCurrentRow() {
        return currentRow.get();
    }

}
