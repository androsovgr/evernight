package ru.evernight.dao.statement;

import lombok.RequiredArgsConstructor;
import ru.evernight.exception.EvernightException;
import ru.evernight.model.Identifiable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RequiredArgsConstructor
public abstract class CrudStatements<T extends Identifiable> {

    private final Class<T> modelClass;
    @PersistenceContext(unitName = "evernight")
    protected EntityManager em;

    public ListAndCount<T> lazyList(int offset, int selectCount) throws EvernightException {
        try {
            CriteriaBuilder criteriaBuilder = em
                    .getCriteriaBuilder();

            CriteriaQuery<Long> countQuery = criteriaBuilder
                    .createQuery(Long.class);
            countQuery.select(criteriaBuilder
                    .count(countQuery.from(modelClass)));
            long count = em.createQuery(countQuery)
                    .getSingleResult();

            CriteriaQuery<T> criteriaQuery = criteriaBuilder
                    .createQuery(modelClass);
            Root<T> from = criteriaQuery.from(modelClass);
            CriteriaQuery<T> select = criteriaQuery.select(from);

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
