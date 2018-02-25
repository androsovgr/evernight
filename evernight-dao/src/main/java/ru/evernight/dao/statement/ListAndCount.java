package ru.evernight.dao.statement;

import lombok.Data;

import java.util.List;
@Data
public class ListAndCount<T> {
    private final List<T> elements;
    private final long totalCount;
}
