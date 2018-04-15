package ru.evernight.ui.filters;

import ru.evernight.dao.statement.filter.DbFilter;

import java.io.Serializable;

public interface UiFIlter<T> extends Serializable {
    void clear();

    DbFilter<T> covert();
}
