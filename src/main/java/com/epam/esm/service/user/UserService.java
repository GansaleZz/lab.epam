package com.epam.esm.service.user;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.util.pagination.PageFilter;

import java.util.List;

public interface UserService {

    /**
     * Searching all users on db.
     * @param paginationFilter object which contains information about page's number
     *                         and number of items for paging.
     * @return list of found users.
     */
    List<UserDto> findAllUsers(PageFilter paginationFilter);

    /**
     * Searching user on db by id.
     * @param userId user's id.
     * @return user if it exists and empty optional if not.
     */
    UserDto findUserById(Long userId);
}
