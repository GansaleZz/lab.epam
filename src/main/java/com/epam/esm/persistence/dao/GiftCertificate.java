package com.epam.esm.persistence.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    @Builder.Default
    private Double price = 0.0;
    @Builder.Default
    private Integer duration = 0;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    @Builder.Default
    private List<Tag> tags = new ArrayList<>();
}
