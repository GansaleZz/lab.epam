package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftCertificateDto {
    private Long id;

    @NotBlank(message = "Name should not be empty")
    private String name;

    @NotBlank(message = "Description should not be empty")
    private String description;

    @NotNull(message = "Price should not be null")
    private BigDecimal price;

    @NotNull(message = "Duration should not be null")
    private Duration duration;

    private LocalDateTime createDate;

    private LocalDateTime lastUpdateDate;

    @Builder.Default
    private List<TagDto> tags = new LinkedList<>();
}
