package ru.evernight.dao.statement.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class DateRangeFilter<T> implements DbFilter<T> {
    @Getter
    @Setter
    private Date from;
    @Getter
    @Setter
    private Date to;
    private Function<Path<T>, Path<Date>> path;

    @Override
    public Predicate apply(CriteriaBuilder cb, Path<T> rootPath) {
        List<Predicate> prs = new ArrayList<>();
        if (from != null) {
            prs.add(cb.greaterThan(path.apply(rootPath), from));
        }
        if (to != null) {
            prs.add(cb.lessThan(path.apply(rootPath), to));
        }
        return cb.and(prs.toArray(new Predicate[2]));
    }

    @Override
    public boolean enabled() {
        return from != null || to != null;
    }
}
