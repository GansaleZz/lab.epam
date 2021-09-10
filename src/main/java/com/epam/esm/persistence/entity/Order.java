package com.epam.esm.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_order")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    private Long orderId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "cost")
    private BigDecimal cost;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id")
    private User usersOrder;
}
