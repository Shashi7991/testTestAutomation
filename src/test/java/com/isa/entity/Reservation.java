package com.isa.entity;

import com.isa.entity.type.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
@ToString
public class Reservation {
    private FlightType flightType;

    private String fromLocation;
    private String toLocation;
    private String departureDate;

    private int adultPassengerCount;
    private int childPassengerCount;
    private int infantPassengerCount;
    List<Passenger> passengers;

    private String outBoundFlightNumber;

    private BundleType bundleType;

    private FareType fareType;

    private PaymentType paymentType;

    private FlexiType flexiType;

    private ConfirmType confirmType;

    private boolean isOnHold;
    private boolean isConfirmed;

    private boolean isCancelReservation = false;
    private boolean isCancelSegment = false;

    private ReturnSegment returnSegment;
}
