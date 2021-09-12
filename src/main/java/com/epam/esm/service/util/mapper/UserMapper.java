package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.entity.User;
import com.epam.esm.service.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements AbstractEntityMapper<UserDto, User> {

    @Override
    public User toEntity(UserDto userDto) {
        return User.builder()
                .userId(userDto.getId())
                .orders(userDto.getOrderList())
                .build();
    }

    @Override
    public UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getUserId())
                .orderList(user.getOrders())
                .build();
    }
}
