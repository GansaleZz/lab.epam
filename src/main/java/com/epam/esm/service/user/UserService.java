package com.epam.esm.service.user;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers(PaginationFilter paginationFilter);

    UserDto findUserById(Long id);
}
