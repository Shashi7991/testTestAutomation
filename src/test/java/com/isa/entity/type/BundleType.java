package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum BundleType {
    BASIC("BASIC"),
    VALUE("VALUE"),
    EXTRA("EXTRA");
    private final String value;
}