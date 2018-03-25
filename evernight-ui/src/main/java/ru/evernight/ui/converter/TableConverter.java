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
    @Inject
    private HallBean hb;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        Object result = super.getAsObject(context, component, value);
        List<Table> ft = hb.getOeb().getFreeTables();
        System.out.println(ft);
        System.out.println(ft.contains(result));
        System.out.println(result);
        System.out.println(ft.get(0).equals(result));
        System.out.println(ft.get(0).getClass() + " - " + result.getClass());
        return result;
    }
}
