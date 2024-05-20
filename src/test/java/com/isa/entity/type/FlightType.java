package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FlightType {
    CONNECTION("Connection"), DIRECT("Direct");

    private final String value;
}
