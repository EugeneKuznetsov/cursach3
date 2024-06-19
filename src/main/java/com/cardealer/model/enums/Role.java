package com.cardealer.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@Getter
public enum Role implements GrantedAuthority {
    ADMIN("Управляющий"),
    MANAGER("Менеджер"),
    USER("Пользователь"),
    ;

    private final String name;

    @Override
    public String getAuthority() {
        return name();
    }
}

