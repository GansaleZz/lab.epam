package com.epam.esm.persistence.jpa.order;

import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderDao implements OrderDao {

    private static final String ID = "id";
    private static final String USERS_ORDER = "usersOrder";
    private static final String EMPTY_STRING = "";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> findAllOrders() {
        return entityManager.createQuery(createQueryByParam(EMPTY_STRING, new Object()))
                .getResultList();
    }

    @Override
    public Optional<Order> findOrderById(Long id) {
        return entityManager.createQuery(createQueryByParam(ID, id))
                .getResultList().stream().findAny();
    }

    @Override
    public List<Order> findOrdersByUserId(Long id) {
        return entityManager.createQuery(createQueryByParam(USERS_ORDER, id))
                .getResultList();
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
        order.ifPresent(value -> entityManager.remove(value));
        return true;
    }

    private CriteriaQuery<Order> createQueryByParam(String attributeName,
                                                    Object attributeValue) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);

        if (!attributeName.equals(EMPTY_STRING)) {
            criteriaQuery.where(criteriaBuilder.equal(root.get(attributeName),
                    attributeValue));
        }
        return criteriaQuery;
    }
}
