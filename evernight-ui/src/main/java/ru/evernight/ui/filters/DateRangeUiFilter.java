package ru.evernight.ui.filters;

import lombok.Getter;
import lombok.Setter;
import ru.evernight.dao.statement.filter.DateRangeFilter;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.ui.func.SerializableFunction;

import javax.persistence.criteria.Path;
import java.util.Date;

public class DateRangeUiFilter<T> implements UiFIlter {
    @Getter
    @Setter
    private Date dateFrom;
    @Getter
    @Setter
    private Date dateTo;
    private final Date defaultDateFrom;
    private final Date defaultDateTo;
    private final SerializableFunction<Path<T>, Path<Date>> path;

    public DateRangeUiFilter(Date defaultDateFrom, Date defaultDateTo, SerializableFunction<Path<T>, Path<Date>> path) {
        dateFrom = this.defaultDateFrom = defaultDateFrom;
        dateTo = this.defaultDateTo = defaultDateTo;
        this.path = path;
    }

    public DateRangeUiFilter(SerializableFunction<Path<T>, Path<Date>> path) {
        this(null, null, path);
    }

    @Override
    public void clear() {
        dateTo = defaultDateTo;
        dateFrom = defaultDateFrom;
    }

    @Override
    public DbFilter covert() {
        return new DateRangeFilter<>(dateFrom, dateTo, path);
    }
}
