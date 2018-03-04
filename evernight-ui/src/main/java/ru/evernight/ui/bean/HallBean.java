package ru.evernight.ui.bean;

import lombok.Getter;
import lombok.Setter;
import ru.evernight.dao.statement.OrderStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.aggregative.AggregatedOrder;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class HallBean implements Serializable{
    @Inject
    private OrderStatements os;
    @Getter
    private List<AggregatedOrder> orders;
    @Getter
    @Setter
    private AggregatedOrder selected;

    @PostConstruct
    private void postConstruct() throws EvernightException {
        update();
        if (!orders.isEmpty()) {
            selected = orders.get(0);
        }
    }

    public void update() throws EvernightException {
        orders = os.openedOrders();
    }
}
