package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum FareType {
    DEFAULT("Default"), SECTOR("Sector");

    private final String value;
}
