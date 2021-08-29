package com.epam.esm.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GiftCertificateDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    @Builder.Default
    private List<TagDto> tags = new LinkedList<>();

    public void addTag(TagDto tagDto){
        tags.add(tagDto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDto that = (GiftCertificateDto) o;
        return name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && duration.equals(that.duration) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, tags);
    }
}
