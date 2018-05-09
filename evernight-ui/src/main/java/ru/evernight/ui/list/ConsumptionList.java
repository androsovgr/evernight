package ru.evernight.ui.list;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.Order;
import ru.evernight.dao.statement.ConsumptionStatements;
import ru.evernight.model.Consumption;
import ru.evernight.model.Consumption_;
import ru.evernight.ui.filters.DateRangeUiFilter;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

@Slf4j
@ViewScoped
@Named
public class ConsumptionList extends ExtendedLazyDataModel<Consumption> {
    @Getter(AccessLevel.PROTECTED)
    @Inject
    private ConsumptionStatements mainStatements;
    @Getter
    private DateRangeUiFilter<Consumption> dateFilter = DateRangeUiFilter.lastMounth(r -> r.get(Consumption_.date));

    @Override
    public void prepareCreate() {
        forCreate = new Consumption();
        forCreate.setDate(new Date());
        forCreate.setType(Consumption.ConsumptionType.OPERATION);
    }

    @Override
    protected Order<Consumption> order() {
        return new Order<>(r -> r.get(Consumption_.date), false);
    }
}
