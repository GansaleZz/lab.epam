package com.epam.esm.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tag extends AbstractEntity{
    private String name;

    @Builder
    public Tag(int id, String name){
        super(id);
        this.name = name;
    }
}
