package com.epam.esm.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class GiftCertificate extends AbstractEntity{
    private String name = null;
    private String description = null;
    private Double price = 0.0;
    private Integer duration = 0;
    private Date createDate;
    private Date lastUpdateDate;
    private String tagName;

    @Builder
    public GiftCertificate(int id, String name, String description, Double price, Integer duration, Date createDate, Date lastUpdateDate){
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
    }
}
