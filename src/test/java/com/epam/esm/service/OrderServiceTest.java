package com.epam.esm.service;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private GiftCertificateDao giftCertificateDao;

    @Mock
    private UserDao userDao;

    @Mock
    private AbstractEntityMapper<OrderDto, Order> orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void findOrdersByUserIdExists() {
        long userId = 1;
        long orderId = 1;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();
        Order order = Order.builder()
                .orderId(orderId)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .build();

        when(orderDao.findOrdersByUserId(paginationFilter, userId))
                .thenReturn(Collections.singletonList(order));
        when(orderMapper.toDto(any()))
                .thenReturn(orderDto);

        assertEquals(1,
                orderService.findOrdersByUserId(paginationFilter, userId).size());
        verify(orderDao, times(1))
                .findOrdersByUserId(any(), any());
    }

    @Test
    void findOrdersByUserIdNotFound() {
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        when(orderDao.findOrdersByUserId(paginationFilter, 123123L))
                .thenReturn(Collections.emptyList());

        assertEquals(0,
                orderService.findOrdersByUserId(paginationFilter, 123123L).size());
        verify(orderDao, times(1))
                .findOrdersByUserId(paginationFilter, 123123L);
    }

    @Test
    void findOrderByIdExists() {
        long userId = 1;
        long orderId = 1;
        Order order = Order.builder()
                .orderId(orderId)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(orderId)
                .build();

        when(orderDao.findOrderById(orderId, userId))
                .thenReturn(Optional.of(order));
        when(orderMapper.toDto(order))
                .thenReturn(orderDto);

        assertEquals(orderDto, orderService.findOrderById(orderId, userId));
        verify(orderDao, times(1))
                .findOrderById(any(), any());
    }

    @Test
    void findOrderByIdNotFoundUserId() {
        long userId = 123123;
        long orderId = 1;

        when(orderDao.findOrderById(orderId, userId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> orderDao.findOrderById(orderId, userId));
        verify(orderDao, times(1))
                .findOrderById(any(), any());
    }

    @Test
    void findOrderByIdNotFound() {
        long orderId = 123123;
        long userId = 1;

        when(orderDao.findOrderById(orderId, userId))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> orderDao.findOrderById(orderId, userId));
        verify(orderDao, times(1))
                .findOrderById(any(), any());
    }

    @Test
    void createOrderSuccess() {
        User user = User.builder()
                .userId(1L)
                .build();
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .giftId(1L)
                .build();
        Order order = Order.builder()
                .orderId(1L)
                .build();
        OrderDto orderDto = OrderDto.builder()
                .id(1L)
                .build();

        when(orderDao.create(giftCertificate, user))
                .thenReturn(order);
        when(userDao.findUserById(1L))
                .thenReturn(Optional.of(user));
        when(giftCertificateDao.findEntityById(1L))
                .thenReturn(Optional.of(giftCertificate));
        when(orderMapper.toDto(order))
                .thenReturn(orderDto);

        assertEquals(orderDto, orderService.create(1L, 1L));
        verify(orderDao, times(1))
                .create(any(), any());
    }
}
