package com.epam.esm.service.user;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface UserService {

    /**
     * Searching all users on db.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @return list of found users.
     */
    List<UserDto> findAllUsers(PaginationFilter paginationFilter);

    /**
     * Searching user on db by id.
     * @param id - user's id.
     * @return user if it exists and empty optional if not.
     */
    UserDto findUserById(Long id);
}
