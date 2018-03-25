package ru.evernight.ui.bean;

import lombok.Getter;
import lombok.Setter;
import ru.evernight.dao.statement.aggregated.AggregatedOrderStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.aggregative.AggregatedOrder;
import ru.evernight.ui.bean.edit.OrderEditBean;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@ViewScoped
@Named
public class HallBean implements Serializable {
    @Inject
    private AggregatedOrderStatements os;
    @Getter
    private List<AggregatedOrder> orders;
    @Getter
    @Setter
    private AggregatedOrder selected;

    @Inject
    @Getter
    private OrderEditBean oeb;

    @PostConstruct
    private void postConstruct() throws EvernightException {
        update();
        if (!orders.isEmpty()) {
            selected = orders.get(0);
        }
    }

    public void update() throws EvernightException {
        orders = os.openedOrders();
        if (!orders.isEmpty() && !orders.contains(selected)) {
            selected = orders.get(0);
        }
    }

    public void createOrder() throws EvernightException {
        oeb.createOrder();
        update();
    }

    public void modifyOrder() throws EvernightException {
        oeb.modifyOrder();
        update();
    }
}
