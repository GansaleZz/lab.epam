package com.epam.esm.service.order;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface OrderService {

    OrderDto findOrderById(Long id, Long userId);

    List<OrderDto> findOrdersByUserId(PaginationFilter paginationFilter, Long id);

    OrderDto create(Long giftId, Long userId);
}
