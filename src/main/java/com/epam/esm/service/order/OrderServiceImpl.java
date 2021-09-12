package com.epam.esm.service.order;

import com.epam.esm.persistence.dao.GiftDao;
import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService{

    private static final String ORDER_NOT_FOUND = "Requested order not found (id = %s)";
    private static final String GIFT_NOT_FOUND = "Requested gift not found (id = %s)";
    private static final String USER_NOT_FOUND = "Requested user not found (id = %s)";

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GiftDao giftDao;

    @Autowired
    private AbstractEntityMapper<OrderDto, Order> orderMapper;

    @Override
    public List<OrderDto> findAllOrders() {
        return orderDao.findAllOrders()
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto findOrderById(Long id) {
        return orderDao.findOrderById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(ORDER_NOT_FOUND,
                        id)));
    }

    @Override
    public List<OrderDto> findOrdersByUserId(Long id) {
        return orderDao.findOrdersByUserId(id)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto create(Long giftId, Long userId) {
        Optional<GiftCertificate> giftCertificate = giftDao.findEntityById(giftId);
        Optional<User> user = userDao.findEntityById(userId);

        return orderMapper.toDto(orderDao.create(
                giftCertificate.orElseThrow(() ->
                        new EntityNotFoundException(String.format(GIFT_NOT_FOUND, giftId))),
                user.orElseThrow(() ->
                        new EntityNotFoundException(String.format(USER_NOT_FOUND, userId)))
        ));
    }

    @Override
    public boolean delete(Long id) {
        return orderDao.delete(id);
    }
}
