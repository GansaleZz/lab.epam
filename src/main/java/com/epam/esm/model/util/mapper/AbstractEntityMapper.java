package com.epam.esm.model.util.mapper;

import com.epam.esm.model.domain.AbstractEntity;
import com.epam.esm.model.dto.AbstractEntityDto;

public interface AbstractEntityMapper<K extends AbstractEntityDto, T extends AbstractEntity>{
    T toEntity(K k);

    K toDto(T t);
}
