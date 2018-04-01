package ru.evernight.dao.statement.filter;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public interface DbFilter<T> {
    Predicate apply(CriteriaBuilder cb, Path<T> rootPath);

    boolean enabled();
}
