package com.cardealer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProductAppStatus {
    WAITING("Ожидание"),
    ACCEPTED("Заказ принят"),
    DONE("Выполнено"),
    ;

    private final String name;
}