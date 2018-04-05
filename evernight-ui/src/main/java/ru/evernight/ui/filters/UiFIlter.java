package ru.evernight.ui.filters;

import ru.evernight.dao.statement.filter.DbFilter;

public interface UiFIlter<T> {
    void clear();

    DbFilter<T> covert();
}
