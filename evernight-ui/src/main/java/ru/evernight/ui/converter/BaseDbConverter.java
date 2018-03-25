package ru.evernight.ui.converter;

import org.apache.commons.lang3.StringUtils;
import ru.evernight.dao.statement.CrudStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Identifiable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

public abstract class BaseDbConverter<T extends Identifiable> implements Converter {
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        try {
            return getStatements().byId(Long.parseLong(value));
        } catch (EvernightException e) {
            throw new ConverterException(e);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        return Long.toString(((Identifiable) value).getId());
    }

    protected abstract CrudStatements<T> getStatements();
}
