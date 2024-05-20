package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ConfirmType {
    FORCE_CONFIRM("FORCE_CONFIRM"),
    PAY_AND_CONFIRM("PAY_AND_CONFIRM");
    private final String value;
}