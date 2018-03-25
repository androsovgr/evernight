package ru.evernight.ui.converter;

import lombok.Getter;
import ru.evernight.dao.statement.TableStatements;
import ru.evernight.model.Table;
import ru.evernight.ui.bean.HallBean;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import java.util.List;

@FacesConverter(forClass = Table.class)
public class TableConverter extends BaseDbConverter<Table> {
    @Inject
    @Getter
    private TableStatements statements;
}
