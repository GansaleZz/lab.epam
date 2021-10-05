package com.epam.esm.service.dto;

import com.epam.esm.persistence.entity.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @Builder.Default
    @JsonIgnore
    private List<Order> orderList = new ArrayList<>();
}
