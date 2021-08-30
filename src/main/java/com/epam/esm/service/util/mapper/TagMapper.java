package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagMapper implements AbstractEntityMapper<TagDto, Tag> {

    @Override
    public Tag toEntity(TagDto tagDto) {
        return Tag.builder()
                .id(tagDto.getId())
                .name(tagDto.getName())
                .build();
    }

    @Override
    public TagDto toDto(Tag tagDao) {
        return TagDto.builder()
                .id(tagDao.getId())
                .name(tagDao.getName())
                .build();
    }
}
