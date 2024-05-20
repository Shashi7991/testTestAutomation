package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PaxType {
    ADULT("ADULT"),
    CHILD("CHILD"),
    INFANT("INFANT");
    private final String value;
}