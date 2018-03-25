package ru.evernight.dao.statement;

import lombok.extern.slf4j.Slf4j;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.ActiveStatus;
import ru.evernight.model.Item;
import ru.evernight.model.Item_;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
@Slf4j
public class ItemStatements extends CrudStatements<Item> {

    public ItemStatements() {
        super(Item.class);
    }

    public List<Item> activeItems() throws EvernightException {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Item> c = cb.createQuery(Item.class);
            Root<Item> root = c.from(Item.class);
            c.where(cb.equal(root.get(Item_.active), ActiveStatus.ACTIVE), cb.equal(root.get(Item_.itemType), Item.ItemType.MAIN_DISH)).orderBy(cb.asc(root.get(Item_.classifier)));
            List<Item> items = em.createQuery(c).getResultList();
            log.debug("Got active items: {}", items);
            return items;
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении к БД", e);
        }
    }

}
