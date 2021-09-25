package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    List<User> findAllEntities(PaginationFilter paginationFilter);

    Optional<User> findEntityById(Long id);

    Long findUserWithTheHighestCost();
}
