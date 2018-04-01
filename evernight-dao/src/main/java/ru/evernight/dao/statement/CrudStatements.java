package ru.evernight.dao.statement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.evernight.dao.statement.filter.DbFilter;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class CrudStatements<T extends Identifiable> {

    public CrudStatements() {
        modelClass = null;
    }

    private final Class<T> modelClass;
    @PersistenceContext(unitName = "evernight")
    protected EntityManager em;

    public ListAndCount<T> lazyList(int offset, int selectCount) throws EvernightException {
        return lazyList(offset, selectCount, Collections.emptyList());
    }

    public ListAndCount<T> lazyList(int offset, int selectCount, List<DbFilter<T>> filters) throws EvernightException {
        try {
            CriteriaBuilder criteriaBuilder = em
                    .getCriteriaBuilder();

            CriteriaQuery<Long> countQuery = criteriaBuilder
                    .createQuery(Long.class);
            countQuery.select(criteriaBuilder
                    .count(countQuery.from(modelClass)));
            List<Predicate> prs = new ArrayList<>();
            for (DbFilter<T> f : filters) {
                if (f.enabled()) {
                    prs.add(f.apply(criteriaBuilder, countQuery.from(modelClass)));
                }
            }
            countQuery.where(prs.toArray(new Predicate[0]));
            long count = em.createQuery(countQuery)
                    .getSingleResult();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder
                    .createQuery(modelClass);
            Root<T> from = criteriaQuery.from(modelClass);
            CriteriaQuery<T> select = criteriaQuery.select(from);
            List<Predicate> prs2 = new ArrayList<>();
            for (DbFilter<T> f : filters) {
                if (f.enabled()) {
                    prs.add(f.apply(criteriaBuilder, from));
                }
            }
            countQuery.where(prs.toArray(new Predicate[0]));
            TypedQuery<T> typedQuery = em.createQuery(select);
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(selectCount);
            List<T> resultList = typedQuery.getResultList();
            return new ListAndCount<>(resultList, count);
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
