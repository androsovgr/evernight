package ru.evernight.dao.statement;

import ru.evernight.model.Order;

import javax.ejb.Stateless;

@Stateless
public class OrderStatements extends CrudStatements<Order> {

    public OrderStatements() {
        super(Order.class);
    }


    public Order byIdInitItems(long id) {
        Order founded = em.find(Order.class, id);
        founded.getItems().size();
        return founded;
    }
}
