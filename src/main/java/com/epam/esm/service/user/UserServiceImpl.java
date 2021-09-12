package com.epam.esm.service.user;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private static final String NOT_FOUND_BY_ID = "Requested user not found (id = %s)";

    @Autowired
    private UserDao userDao;

    @Autowired
    private AbstractEntityMapper<UserDto, User> userMapper;

    @Override
    public List<UserDto> findAllUsers() {
        return userDao.findAllEntities()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserById(Long id) {
        return userDao.findEntityById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_ID,
                        id)));
    }
}
