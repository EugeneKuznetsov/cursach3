package com.cardealer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductCategory {
    BMW("BMW"),
    Volvo("Volvo"),
    Chevrolet("Chevrolet"),
    Lada("Lada"),
    Mercedes("Mercedes"),
    ;

    private final String name;
}