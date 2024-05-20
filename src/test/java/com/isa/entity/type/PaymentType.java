package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaymentType {
    CASH("Cash"),
    OFFLINE("Offline Payment"),
    CREDIT_CARD("Credit Card Non 3DS"),
    ON_ACCOUNT("On Account"),
    ON_HOLD("ON_HOLD");
    private final String value;
}