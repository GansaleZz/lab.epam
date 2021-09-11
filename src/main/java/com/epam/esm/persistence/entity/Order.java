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
@RequiredArgsConstructor
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue
    @NonNull
    private Long orderId;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "cost")
    @NonNull
    private BigDecimal cost;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "gift_id")
    private GiftCertificate giftCertificate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="user_id")
    private User usersOrder;
}
