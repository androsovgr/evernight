package ru.evernight.dao;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.criteria.Path;
import java.util.function.Function;

@Data
@AllArgsConstructor
public class Order<T> {
    private Function<Path<T>, Path<?>> path;
    private boolean asc;
}
