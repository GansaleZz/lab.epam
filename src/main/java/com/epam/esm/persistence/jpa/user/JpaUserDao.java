package com.epam.esm.persistence.jpa.user;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserDao implements UserDao {

    private static final String USER_ID = "userId";
    private static final String COST = "cost";
    private static final String USERS_ORDER = "usersOrder";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllEntities() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);

        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Optional<User> findEntityById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.and(criteriaBuilder
                .equal(root.get(USER_ID), id)));

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findAny();
    }

    @Override
    public Long findUserWithTheHighestCost() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Order, User> join = root.join(USERS_ORDER, JoinType.LEFT);

        criteriaQuery.multiselect( join.get(USER_ID),
                criteriaBuilder.sum(root.get(COST)))
            .groupBy(join.get(USER_ID))
            .orderBy(criteriaBuilder.desc(criteriaBuilder.sum(root.get(COST))));

        return (Long) Arrays.stream(entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findAny()
                .get())
                .findAny()
                .get();
    }
}
