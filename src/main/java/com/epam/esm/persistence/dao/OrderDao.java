package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface OrderDao {

    Optional<Order> findOrderById(Long id);

    List<Order> findOrdersByUserId(Long id);

    Order create(GiftCertificate giftCertificate, User user);
}
