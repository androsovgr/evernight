package ru.evernight.ui.filters;

import ru.evernight.dao.statement.filter.DbFilter;

public interface UiFIlter {
    void clear();

    DbFilter covert();
}
