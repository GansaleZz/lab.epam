package com.epam.esm.service.order;

import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface OrderService {

    OrderDto findOrderById(Long id);

    List<OrderDto> findOrdersByUserId(Long id);

    OrderDto create(Long giftId, Long userId);
}
