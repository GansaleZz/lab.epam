package com.epam.esm.persistence.jpa.order;

import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PaginationFilter;
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

    private static final String ORDER_ID = "orderId";
    private static final String USERS_ORDER = "usersOrder";
    private static final String EMPTY_STRING = "";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> findOrderById(Long id, Long userId) {
        Optional<Order> order = entityManager.createQuery(createQueryByParam(ORDER_ID, id))
                .getResultList()
                .stream()
                .findAny();
        if (order.isPresent() &&
            order.get().getUsersOrder().getUserId().equals(userId)) {
            return order;
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findOrdersByUserId(PaginationFilter paginationFilter,
                                          Long id) {
        paginationFilter.setCount(entityManager
                .createQuery(createQueryByParam(USERS_ORDER, id))
                .getResultList().size());

        return entityManager.createQuery(createQueryByParam(USERS_ORDER, id))
                .setFirstResult(paginationFilter.getPage() * paginationFilter.getItems())
                .setMaxResults(paginationFilter.getItems())
                .getResultList();
    }

    @Override
    @Transactional
    public Order create(GiftCertificate giftCertificate, User user) {
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
