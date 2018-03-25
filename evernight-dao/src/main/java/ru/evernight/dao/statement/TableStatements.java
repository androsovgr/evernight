package ru.evernight.dao.statement;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Table;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
@Slf4j
public class TableStatements extends CrudStatements<Table> {

    public TableStatements() {
        super(Table.class);
    }

    public List<Table> freeTables() throws EvernightException {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            em.unwrap(Session.class).enableFilter("activeOrders");
            CriteriaQuery<Table> c = cb.createQuery(Table.class);
            c.from(Table.class);
            List<Table> tables = em.createQuery(c).getResultList();
            tables.removeIf(t -> !t.getOrders().isEmpty());
            log.debug("Got free tables: {}", tables);
            return tables;
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении к БД", e);
        }
    }

}
