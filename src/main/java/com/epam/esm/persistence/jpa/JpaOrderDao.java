package com.epam.esm.persistence.jpa;

import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaOrderDao implements OrderDao {

    private static final String USERS_ORDER = "usersOrder";

    @PersistenceContext
    private EntityManager entityManager;

    private final JpaDaoHelper<Order> jpaDaoHelper;

    @Autowired
    public JpaOrderDao(JpaDaoHelper<Order> jpaDaoHelper) {
        this.jpaDaoHelper = jpaDaoHelper;
    }

    @Override
    public Optional<Order> findOrderById(Long orderId) {
        return Optional.ofNullable(entityManager.find(Order.class, orderId));
    }

    @Override
    public List<Order> findAllOrdersByUserId(PageFilter pageFilter,
                                             Long userId) {
        pageFilter.setCount(countResult(userId));

        return entityManager.createQuery(jpaDaoHelper
                        .createQueryByParam(USERS_ORDER, userId, Order.class))
                .setFirstResult(pageFilter.getPage() * pageFilter.getItems())
                .setMaxResults(pageFilter.getItems())
                .getResultList();
    }

    @Override
    @Transactional
    public Order createOrder(GiftCertificate giftCertificate, User user) {
        Order order = Order.builder()
                .usersOrder(user)
                .cost(giftCertificate.getPrice())
                .timestamp(LocalDateTime.now())
                .giftCertificate(giftCertificate)
                .build();

        entityManager.persist(order);
        user.getOrders().add(order);
        entityManager.merge(user);

        return order;
    }

    private long countResult(Long userId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Order> root = cq.from(Order.class);
        cq.select(criteriaBuilder.count(root))
                .where(criteriaBuilder.equal(root.get(USERS_ORDER), userId));

        return entityManager.createQuery(cq).getSingleResult();
    }
}
