package com.isa.entity;

import com.isa.entity.type.BaggageType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Ancillary {
    private BaggageType baggageType;
    private Integer mealCount;
}
