package com.epam.esm.service.user;

import com.epam.esm.service.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsers();

    UserDto findUserById(Long id);
}
