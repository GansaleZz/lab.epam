package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    /**
     * Searching all users on db.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @return list of found users.
     */
    List<User> findAllUsers(PaginationFilter paginationFilter);

    /**
     * Searching user on db by id.
     * @param id - user's id.
     * @return user if it exists and empty optional if not.
     */
    Optional<User> findUserById(Long id);

    /**
     * Searching user with the highest cost of all orders.
     * @return id of found user.
     */
    Long findUserWithTheHighestOrdersCost();
}
