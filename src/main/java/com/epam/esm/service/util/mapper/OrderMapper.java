package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements AbstractEntityMapper<OrderDto, Order> {

    @Override
    public Order toEntity(OrderDto orderDto) {
        return Order.builder()
                .orderId(orderDto.getId())
                .cost(orderDto.getCost())
                .timestamp(orderDto.getTimestamp())
                .build();
    }

    @Override
    public OrderDto toDto(Order order) {
        return OrderDto.builder()
                .id(order.getOrderId())
                .cost(order.getCost())
                .timestamp(order.getTimestamp())
                .build();
    }
}
