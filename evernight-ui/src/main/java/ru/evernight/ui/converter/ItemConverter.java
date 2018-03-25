package ru.evernight.ui.converter;

import lombok.Getter;
import ru.evernight.dao.statement.ItemStatements;
import ru.evernight.model.Item;

import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter(forClass = Item.class)
public class ItemConverter extends BaseDbConverter<Item> {
    @Inject
    @Getter
    private ItemStatements statements;
}
