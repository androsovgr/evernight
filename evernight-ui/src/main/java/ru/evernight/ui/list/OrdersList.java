package ru.evernight.ui.list;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.statement.aggregated.AggregatedOrderStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Order_;
import ru.evernight.model.aggregative.AggregatedOrder;
import ru.evernight.model.aggregative.AggregatedOrder_;
import ru.evernight.ui.filters.DateRangeUiFilter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Calendar;
import java.util.Date;

import static org.apache.commons.lang3.time.DateUtils.*;

@Slf4j
@ViewScoped
@Named
public class OrdersList extends ExtendedLazyDataModel<AggregatedOrder> {
    @Inject
    @Getter(AccessLevel.PROTECTED)
    private AggregatedOrderStatements mainStatements;

    @Getter
    private final DateRangeUiFilter<AggregatedOrder> orderClosedDateFilter;

    public OrdersList() {
        Date todayMidnight = addSeconds(ceiling(new Date(), Calendar.DATE), -1);
        Date monthAgo = addMonths(truncate(new Date(), Calendar.DATE), -1);
        this.orderClosedDateFilter = new DateRangeUiFilter<>(monthAgo, todayMidnight, root -> root.get(AggregatedOrder_.order).get(Order_.closeTime));
    }

}
