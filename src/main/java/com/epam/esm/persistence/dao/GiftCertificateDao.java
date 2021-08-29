package com.epam.esm.persistence.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiftCertificateDao {
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
    private List<TagDao> tags = new ArrayList<>();

    public void addTag(TagDao tagDao){
        tags.add(tagDao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDao that = (GiftCertificateDao) o;
        return Objects.equals(id, that.id) && name.equals(that.name) && description.equals(that.description) && price.equals(that.price) && duration.equals(that.duration) && Objects.equals(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, tags);
    }
}
