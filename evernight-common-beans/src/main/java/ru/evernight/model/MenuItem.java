package ru.evernight.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MenuItem {
    private String label;
    private String path;
}
