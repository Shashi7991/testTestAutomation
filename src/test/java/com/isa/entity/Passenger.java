package com.isa.entity;

import com.isa.entity.type.PaxType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class Passenger {
    private String title;
    private String firstName;
    private String lastName;
    private String nationality;
    private String dob;
    private PaxType paxType;
    private Integer withAdult; // For Infant PaxType
    private Ancillary ancillary;
    private Ancillary returnAncillary;
}
