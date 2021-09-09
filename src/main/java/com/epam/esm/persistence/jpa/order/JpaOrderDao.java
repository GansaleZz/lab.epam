package com.epam.esm.persistence.jpa.order;

import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderDao implements OrderDao {

    private static final String ID = "id";
    private static final String ORDERS = "orders";
    private static final String USER_ID = "userId";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAllOrders() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> allOrders = criteriaQuery.select(root);

        return entityManager.createQuery(allOrders).getResultList();
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        CriteriaQuery<Order> order = criteriaQuery.select(root);
        order.where(criteriaBuilder.equal(root.get(ID), id));

        return entityManager.createQuery(order).getResultList().stream().findAny();
    }

    @Override
    public List<Order> findOrdersByUserId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> rootUser = criteriaQuery.from(User.class);

        Predicate predicate = criteriaBuilder.and(criteriaBuilder
                .equal(rootUser.get(USER_ID), id));

        criteriaQuery.select(rootUser.get(ORDERS));
        criteriaQuery.where(predicate);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    @Transactional
    public Order create(Order order, User user) {
        entityManager.persist(order);

        user.getOrders().add(order);
        entityManager.merge(user);
        return order;
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<Order> order = findOrderById(id);
        if (order.isPresent()) {
            entityManager.remove(order.get());
            return true;
        } else {
            return false;
        }
    }
}
