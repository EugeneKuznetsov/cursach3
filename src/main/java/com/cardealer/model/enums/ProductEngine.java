package com.cardealer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductEngine {
    OIL("Бензин"),
    DIESEL("Дизель"),
    HYBRID("Габрид"),
    ELECTRON("Электро"),
    ;

    private final String name;
}