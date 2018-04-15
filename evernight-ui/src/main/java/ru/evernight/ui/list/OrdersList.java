package ru.evernight.ui.list;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.statement.aggregated.AggregatedOrderStatements;
import ru.evernight.model.Order_;
import ru.evernight.model.aggregative.AggregatedOrder;
import ru.evernight.model.aggregative.AggregatedOrder_;
import ru.evernight.ui.filters.DateRangeUiFilter;

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

    @Getter
    private final DateRangeUiFilter<AggregatedOrder> orderClosedDateFilter = DateRangeUiFilter.lastMounth(root -> root.get(AggregatedOrder_.order).get(Order_.closeTime));

}
