package ru.evernight.ui.list;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.evernight.dao.statement.CrudStatements;
import ru.evernight.dao.statement.ListAndCount;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Identifiable;
import ru.evernight.ui.filters.UiFIlter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ExtendedLazyDataModel<T extends Identifiable> extends LazyDataModel<T> {

    @Getter
    @Setter
    protected T selected;
    @Getter
    protected T forCreate;

    @SuppressWarnings("unchecked")
    protected List<DbFilter<T>> filters() {
        // TODO add type check
        List<DbFilter> l = Arrays.stream(getClass().getMethods())
                .filter(m -> UiFIlter.class.isAssignableFrom(m.getReturnType()))
                .filter(m -> m.getParameterCount() == 0)
                .map(m -> UiFIlter.class.cast(invoke(m, this)).covert())
                .collect(Collectors.toList());
        return  (List<DbFilter<T>>) (List) l;
    }


    @SneakyThrows(EvernightException.class)
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        ListAndCount<T> result = getMainStatements().lazyList(first, pageSize, filters());
        setRowCount((int) result.getTotalCount());
        if (!result.getElements().contains(selected) && !result.getElements().isEmpty()) {
            selected = result.getElements().get(0);
        }
        return result.getElements();
    }

    @SneakyThrows
    private Object invoke(Method m, Object o) {
        return m.invoke(o);
    }

    protected abstract CrudStatements<T> getMainStatements();

    public void modify() throws EvernightException {
        getMainStatements().update(selected);
    }

    public void delete() throws EvernightException {
        getMainStatements().delete(selected.getId());
    }

    @Override
    public Object getRowKey(T object) {
        return object.getId();
    }

    @SneakyThrows(EvernightException.class)
    @Override
    public T getRowData(String rowKey) {
        return getMainStatements().byId(Long.parseLong(rowKey));
    }
}
