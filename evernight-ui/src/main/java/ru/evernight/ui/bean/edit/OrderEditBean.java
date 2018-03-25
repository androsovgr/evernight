package ru.evernight.ui.bean.edit;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.statement.ItemStatements;
import ru.evernight.dao.statement.OrderStatements;
import ru.evernight.dao.statement.TableStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Item;
import ru.evernight.model.Order;
import ru.evernight.model.OrderItem;
import ru.evernight.model.Table;
import ru.evernight.ui.bean.LoginBean;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class OrderEditBean implements Serializable {
    @Getter
    private List<Table> freeTables;
    @Getter
    private List<Item> availableItems;
    @Getter
    private Order order;
    @Getter
    @Setter
    private Item selectedItem;

    @Inject
    private TableStatements ts;
    @Inject
    private ItemStatements is;
    @Inject
    private OrderStatements os;
    @Inject
    private LoginBean lb;

    public void prepareCreate() throws EvernightException {
        freeTables = ts.freeTables();
        availableItems = is.activeItems();
        order = Order.builder().openTime(new Date()).table(freeTables.isEmpty() ? null : freeTables.get(0)).waiter(lb.getUser()).items(new ArrayList<>()).build();
    }

    public void prepareModify(long id) throws EvernightException {
        order = os.byIdInitItems(id);
        freeTables = ts.freeTables();
        freeTables.add(order.getTable());
        availableItems = is.activeItems();
    }

    public void remove(OrderItem oi) {
        order.getItems().remove(oi);
    }

    public void addItem() {
        OrderItem oi = OrderItem.builder().item(selectedItem).time(new Date()).order(order).build();
        order.getItems().add(0, oi);
    }

    public List<Item> findItems(String query) {
        log.debug("Called findItems with q={}. Now have items: {}", query, availableItems);
        return availableItems.stream().filter(i -> i.getLabel().toUpperCase().contains(query.toUpperCase())).collect(Collectors.toList());
    }

    public void createOrder() throws EvernightException {
        os.create(order);
    }

    public void modifyOrder() throws EvernightException {
        os.update(order);
    }
}
