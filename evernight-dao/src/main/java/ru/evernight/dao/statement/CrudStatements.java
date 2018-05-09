package ru.evernight.dao.statement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.Order;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("EjbInterceptorInspection")
@Slf4j
@RequiredArgsConstructor
public abstract class CrudStatements<T extends Identifiable> {

    private final Class<T> modelClass;
    @PersistenceContext(unitName = "evernight")
    protected EntityManager em;

    public ListAndCount<T> lazyList(int offset, int selectCount, List<DbFilter<T>> filters, Order<T> order) throws EvernightException {
        log.debug("Started with offset={}, selectCount={}, filters={}", offset, selectCount, filters);
        try {
            CriteriaBuilder criteriaBuilder = em
                    .getCriteriaBuilder();

            CriteriaQuery<Long> countQuery = criteriaBuilder
                    .createQuery(Long.class);
            Root<T> root = countQuery.from(modelClass);
            countQuery.select(criteriaBuilder.count(root));
            List<Predicate> prs = new ArrayList<>();
            for (DbFilter<T> f : filters) {
                if (f.enabled()) {
                    prs.add(f.apply(criteriaBuilder, root));
                }
            }
            countQuery.where(prs.toArray(new Predicate[prs.size()]));
            long count = em.createQuery(countQuery)
                    .getSingleResult();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder
                    .createQuery(modelClass);
            Root<T> from = criteriaQuery.from(modelClass);
            CriteriaQuery<T> select = criteriaQuery.select(from);
            select.where(prs.toArray(new Predicate[prs.size()]));
            if (order != null) {
                Path<?> orderPath = order.getPath().apply(root);
                select.orderBy(order.isAsc() ? criteriaBuilder.asc(orderPath) : criteriaBuilder.desc(orderPath));
            }
            TypedQuery<T> typedQuery = em.createQuery(select);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(selectCount);
            List<T> resultList = typedQuery.getResultList();
            ListAndCount<T> result = new ListAndCount<>(resultList, count);
            log.debug("Got result: {}", result);
            return result;
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении к БД", e);
        }
    }

    public List<T> list() throws EvernightException {
        return list(new ArrayList<>());
    }

    public List<T> list(List<DbFilter<T>> filters) throws EvernightException {
        log.debug("Started with filters={}", filters);
        try {
            CriteriaBuilder criteriaBuilder = em
                    .getCriteriaBuilder();
            CriteriaQuery<T> criteriaQuery = criteriaBuilder
                    .createQuery(modelClass);
            Root<T> root = criteriaQuery.from(modelClass);
            List<Predicate> prs = new ArrayList<>();
            for (DbFilter<T> f : filters) {
                if (f.enabled()) {
                    prs.add(f.apply(criteriaBuilder, root));
                }
            }
            CriteriaQuery<T> select = criteriaQuery.select(root);
            select.where(prs.toArray(new Predicate[prs.size()]));
            TypedQuery<T> typedQuery = em.createQuery(select);
            List<T> resultList = typedQuery.getResultList();
            log.debug("Got result: {}", resultList);
            return resultList;
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении к БД", e);
        }
    }

    public T byId(long id) throws EvernightException {
        try {
            return em.find(modelClass, id);
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);
        }
    }

    public long create(T t) throws EvernightException {
        try {
            em.persist(t);
            return t.getId();
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);

        }
    }

    public void update(T t) throws EvernightException {
        try {
            em.merge(t);
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);

        }
    }

    public void delete(long id) throws EvernightException {
        try {
            T founded = em.find(modelClass, id);
            if (founded == null) {
                throw new EvernightException("Объект с id=" + id + " не найден");
            }
            em.remove(founded);
        } catch (PersistenceException e) {
            throw new EvernightException("Ошибка при обращении в БД", e);

        }
    }
}
