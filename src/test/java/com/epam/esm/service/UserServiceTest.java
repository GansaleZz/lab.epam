package com.epam.esm.service;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @Mock
    private AbstractEntityMapper<UserDto, User> userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void findAllUsers() {
        User user = User.builder().userId(1L).build();
        UserDto userDto = UserDto.builder().id(1L).build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        when(userDao.findAllUsers(paginationFilter))
                .thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(any()))
                .thenReturn(userDto);

        assertEquals(Collections.singletonList(userDto),
                userService.findAllUsers(paginationFilter));
        verify(userDao, times(1))
                .findAllUsers(paginationFilter);
    }

    @Test
    void findUserByIdExists() {
        User user = User.builder()
                .userId(1L)
                .build();
        UserDto userDto = UserDto.builder()
                .id(1L)
                .build();

        when(userDao.findUserById(1L))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(any()))
                .thenReturn(userDto);

        assertEquals(userDto, userService.findUserById(1L));
        verify(userDao, times(1))
                .findUserById(any());
    }

    @Test
    void findUserByIdNotFound() {
        when(userDao.findUserById(123L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> userService.findUserById(123L));
        verify(userDao, times(1))
                .findUserById(any());
    }
}
