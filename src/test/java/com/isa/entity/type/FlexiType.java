package com.isa.entity.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)

public enum FlexiType {
    NO_FLEX("No Bag"),
    VALUE_FLEX("Value Flex"),
    VALUE("Value"),
    ALL_IN_FLEX("All IN Flex");
    private final String value;
}