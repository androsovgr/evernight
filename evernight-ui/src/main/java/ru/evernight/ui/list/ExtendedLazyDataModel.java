package ru.evernight.ui.list;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.evernight.dao.Order;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class ExtendedLazyDataModel<T extends Identifiable> extends LazyDataModel<T> {

    @Getter
    @Setter
    protected T selected;
    @Getter
    protected T forCreate;
    @Setter
    private Consumer<T> selectionCallback;

    @SuppressWarnings("unchecked")
    protected List<DbFilter<T>> filters() {
        // TODO add type check
        List<DbFilter> l = Arrays.stream(getClass().getMethods())
                .filter(m -> UiFIlter.class.isAssignableFrom(m.getReturnType()))
                .filter(m -> m.getParameterCount() == 0)
                .map(m -> UiFIlter.class.cast(invoke(m, this)).covert())
                .collect(Collectors.toList());
        return (List<DbFilter<T>>) (List) l;
    }

    private void callback(T newSelected) {
        if (selectionCallback != null) {
            selectionCallback.accept(newSelected);
        }
    }


    @SneakyThrows(EvernightException.class)
    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        ListAndCount<T> result = getMainStatements().lazyList(first, pageSize, filters(),order());
        setRowCount((int) result.getTotalCount());
        if (!result.getElements().contains(selected) && !result.getElements().isEmpty()) {
            selected = result.getElements().get(0);
            callback(selected);
        }
        return result.getElements();
    }

    @SneakyThrows
    private Object invoke(Method m, Object o) {
        return m.invoke(o);
    }

    protected abstract CrudStatements<T> getMainStatements();

    public void prepareCreate() throws EvernightException {

    }

    public void create() throws EvernightException {
        getMainStatements().create(forCreate);
    }

    public void prepareModify() throws EvernightException {

    }

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
        T result = getMainStatements().byId(Long.parseLong(rowKey));
        callback(result);
        return result;
    }

    protected Order<T> order(){
        return null;
    }
}
