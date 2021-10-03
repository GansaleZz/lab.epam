package com.epam.esm.service;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
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
        PageFilter pageFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        when(userDao.findAllUsers(pageFilter))
                .thenReturn(Collections.singletonList(user));
        when(userMapper.toDto(any()))
                .thenReturn(userDto);

        List<UserDto> actualList = userService.findAllUsers(pageFilter);

        assertEquals(Collections.singletonList(userDto), actualList);
        verify(userDao, times(1))
                .findAllUsers(pageFilter);
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

        UserDto actualUserDto =  userService.findUserById(1L);

        assertEquals(userDto, actualUserDto);
        verify(userDao, times(1))
                .findUserById(any());
    }

    @Test
    void findUserByIdNotFound() {
        when(userDao.findUserById(123L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> userService.findUserById(123L));
        verify(userDao, times(1))
                .findUserById(any());
    }
}
