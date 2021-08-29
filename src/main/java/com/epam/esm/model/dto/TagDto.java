package com.epam.esm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class TagDto extends AbstractEntityDto {
    private String name;

    @Builder
    public TagDto(int id, String name){
        super(id);
        this.name = name;
    }
}
