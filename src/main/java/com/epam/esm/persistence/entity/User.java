package com.epam.esm.persistence.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "usersOrder", fetch= FetchType.LAZY)
    @Builder.Default
    List<Order> orders = new ArrayList<>();
}
