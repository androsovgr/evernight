package ru.evernight.ui.list;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.Order;
import ru.evernight.dao.statement.aggregated.AggregatedOrderStatements;
import ru.evernight.model.Order_;
import ru.evernight.model.aggregative.AggregatedOrder;
import ru.evernight.model.aggregative.AggregatedOrder_;
import ru.evernight.ui.bean.edit.OrderEditBean;
import ru.evernight.ui.filters.DateRangeUiFilter;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Slf4j
@ViewScoped
@Named
public class OrdersList extends ExtendedLazyDataModel<AggregatedOrder> {
    @Inject
    @Getter(AccessLevel.PROTECTED)
    private AggregatedOrderStatements mainStatements;
    @Inject
    @Getter
    private OrderEditBean oeb;

    @Getter
    private final DateRangeUiFilter<AggregatedOrder> orderClosedDateFilter = DateRangeUiFilter.lastMounth(root -> root.get(AggregatedOrder_.order).get(Order_.closeTime));

    @PostConstruct
    private void postConstruct() {
        oeb.setManagerMode(true);
        setSelectionCallback(ao -> oeb.setOrder(ao.getOrder()));
    }

    @Override
    protected Order<AggregatedOrder> order() {
        return new Order<>(r -> r.get(AggregatedOrder_.order).get(Order_.closeTime), false);
    }
}
