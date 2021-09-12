package com.epam.esm.service.dto;

import com.epam.esm.persistence.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {

    private Long id;

    private LocalDateTime timestamp;

    private BigDecimal cost;

    @JsonIgnore
    private User user;
}
