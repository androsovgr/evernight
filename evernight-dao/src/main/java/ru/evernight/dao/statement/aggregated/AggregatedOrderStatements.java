package ru.evernight.dao.statement.aggregated;

import ru.evernight.dao.statement.CrudStatements;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Order;
import ru.evernight.model.Order_;
import ru.evernight.model.Table_;
import ru.evernight.model.aggregative.AggregatedOrder;
import ru.evernight.model.aggregative.AggregatedOrder_;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class AggregatedOrderStatements extends CrudStatements<AggregatedOrder> {

    public AggregatedOrderStatements() {
        super(AggregatedOrder.class);
    }

    public List<AggregatedOrder> openedOrders() throws EvernightException {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<AggregatedOrder> c = cb.createQuery(AggregatedOrder.class);
            Root<AggregatedOrder> root = c.from(AggregatedOrder.class);
            Path<Order> order = root.get(AggregatedOrder_.order);
            c.where(cb.isNull(order.get(Order_.closeTime))).orderBy(cb.asc(order.get(Order_.table).get(Table_.label)));
            return em.createQuery(c).getResultList();
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении к БД", e);
        }
    }
}
