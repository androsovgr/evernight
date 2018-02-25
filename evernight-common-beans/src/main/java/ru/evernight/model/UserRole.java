package ru.evernight.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {

    WAITER("Официант"), MANAGER("Менеджер"), ADMIN("Администратор системы");

    private final String label;
}
