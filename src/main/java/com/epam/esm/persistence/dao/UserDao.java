package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAllEntities();

    Optional<User> findEntityById(Long id);

    Long findUserWithTheHighestCost();
}
