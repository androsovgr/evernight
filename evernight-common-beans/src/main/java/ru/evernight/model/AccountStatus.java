package ru.evernight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountStatus {
    ACTIVE("Активен"), LOCKED("Заблокирован");

    private final String label;
}
