package ru.evernight.dao.statement;

import ru.evernight.model.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
public class UserStatements {
    @PersistenceContext(unitName = "evernight")
    private EntityManager em;

    public List<User> allUsers() {
        CriteriaBuilder criteriaBuilder = em
                .getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder
                .createQuery(User.class);
        Root<User> from = criteriaQuery.from(User.class);
        CriteriaQuery<User> select = criteriaQuery.select(from);
        TypedQuery<User> typedQuery = em.createQuery(select);
        return typedQuery.getResultList();
    }
}
