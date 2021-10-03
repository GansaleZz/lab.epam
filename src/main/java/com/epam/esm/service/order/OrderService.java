package com.epam.esm.service.order;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.util.pagination.PageFilter;

import java.util.List;

public interface OrderService {

    /**
     * Searching order on db by id.
     * @param orderId order's id.
     * @return order if it exists and empty optional if not.
     */
    OrderDto findOrderById(Long orderId);

    /**
     * Searching all orders of user on db.
     * @param paginationFilter object which contains information about page's number
     *                         and number of items for paging.
     * @param userId user's id.
     * @return list of found orders.
     */
    List<OrderDto> findAllOrdersByUserId(PageFilter paginationFilter, Long userId);

    /**
     * Creating order on db.
     * @param giftCertificateId the gift certificate's id that the user is buying.
     * @param userId user's id, which buying gift certificate.
     * @return created order.
     */
    OrderDto createOrder(Long giftCertificateId, Long userId);
}
