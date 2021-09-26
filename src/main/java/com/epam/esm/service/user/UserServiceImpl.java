package com.epam.esm.service.user;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private static final String NOT_FOUND_BY_ID = "Requested user not found (id = %s)";
    private final UserDao userDao;
    private final AbstractEntityMapper<UserDto, User> userMapper;

    @Autowired
    public UserServiceImpl(UserDao userDao, AbstractEntityMapper<UserDto, User> userMapper) {
        this.userDao = userDao;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDto> findAllUsers(PaginationFilter paginationFilter) {
        return userDao.findAllUsers(paginationFilter)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long userId) {
        return userDao.findUserById(userId)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_ID,
                        userId)));
    }
}
