package com.epam.esm.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @OneToMany
    @Builder.Default
    @JoinTable(
            name = "user_orders",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "order_id")
    )
    List<Order> orders = new ArrayList<>();
}
