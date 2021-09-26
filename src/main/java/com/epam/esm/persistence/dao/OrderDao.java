package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    /**
     * Searching all orders of user on db.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param userId - user's id.
     * @return list of found orders.
     */
    List<Order> findOrdersByUserId(PaginationFilter paginationFilter, Long userId);

    /**
     * Searching order on db by id.
     * @param orderId - order's id.
     * @param userId - user's id.
     * @return order if it exists and empty optional if not.
     */
    Optional<Order> findOrderById(Long orderId, Long userId);

    /**
     * Creating order on db.
     * @param giftCertificate - the gift certificate that the user is buying.
     * @param user - user, which buying gift certificate.
     * @return created order.
     */
    Order create(GiftCertificate giftCertificate, User user);
}
