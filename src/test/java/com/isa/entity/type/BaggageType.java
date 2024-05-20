package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BaggageType {
    NO_BAGGAGE("No Bag"),
    BAGGAGE_23("23 Kg 1 Piece"),
    BAGGAGE_30("30 Kg Total in 2 Piece"),
    BAGGAGE_46("46 Kg Total in 2 Piece");
    private final String value;
}