package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    List<Order> findAllOrders();

    Optional<Order> findOrderById(Long id);

    List<Order> findOrdersByUserId(Long id);

    // user's id
    Order create(Order order, User user);

    boolean delete(Long id);
}
