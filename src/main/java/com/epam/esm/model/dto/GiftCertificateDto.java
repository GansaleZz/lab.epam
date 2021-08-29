package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateDto extends AbstractEntityDto {
    private String name = null;
    private String description = null;
    private Double price = 0.0;
    private Integer duration = 0;
    private Date createDate;
    private Date lastUpdateDate;
    private String tagName;

    @Builder
    public GiftCertificateDto(int id, String name, String description, Double price, Integer duration, Date createDate, Date lastUpdateDate){
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
