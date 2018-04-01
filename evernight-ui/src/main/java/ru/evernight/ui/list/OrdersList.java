package ru.evernight.ui.list;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.evernight.dao.statement.ListAndCount;
import ru.evernight.dao.statement.aggregated.AggregatedOrderStatements;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.aggregative.AggregatedOrder;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

@Slf4j
@ViewScoped
@Named
public class OrdersList extends LazyDataModel<AggregatedOrder> {
    @Inject
    private AggregatedOrderStatements aos;

    @Getter
    @Setter
    private AggregatedOrder selected;

    @SneakyThrows(EvernightException.class)
    @Override
    public List<AggregatedOrder> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map filters) {
        ListAndCount<AggregatedOrder> result = aos.lazyList(0, 10, filters());
        setRowCount((int) result.getTotalCount());
        if (!result.getElements().contains(selected) && !result.getElements().isEmpty()) {
            selected = result.getElements().get(0);
        }
        return result.getElements();
    }

    protected List<DbFilter<AggregatedOrder>> filters() {

    }


    public void modify() throws EvernightException {
        aos.update(selected);
    }

    public void delete() throws EvernightException {
        aos.delete(selected.getId());
    }

    @Override
    public Object getRowKey(AggregatedOrder object) {
        return object.getId();
    }

    @SneakyThrows(EvernightException.class)
    @Override
    public AggregatedOrder getRowData(String rowKey) {
        return aos.byId(Long.parseLong(rowKey));
    }
}
