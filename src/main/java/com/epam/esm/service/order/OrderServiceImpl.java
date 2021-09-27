package com.epam.esm.service.order;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final GiftCertificateDao giftDao;
    private final AbstractEntityMapper<OrderDto, Order> orderMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao,
                            UserDao userDao,
                            GiftCertificateDao giftDao,
                            AbstractEntityMapper<OrderDto, Order> orderMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.giftDao = giftDao;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderDto findOrderById(Long orderId, Long userId) {
        return orderDao.findOrderById(orderId, userId)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(orderId.toString()));
    }

    @Override
    public List<OrderDto> findOrdersByUserId(PaginationFilter paginationFilter,
                                             Long userId) {
        return orderDao.findOrdersByUserId(paginationFilter, userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto create(Long giftCertificateId, Long userId) {
        Optional<GiftCertificate> giftCertificate = giftDao.findEntityById(giftCertificateId);
        Optional<User> user = userDao.findUserById(userId);

        return orderMapper.toDto(orderDao.create(
                giftCertificate.orElseThrow(() ->
                        new EntityNotFoundException(giftCertificateId.toString())),
                user.orElseThrow(() ->
                        new EntityNotFoundException(userId.toString()))
        ));
    }
}
