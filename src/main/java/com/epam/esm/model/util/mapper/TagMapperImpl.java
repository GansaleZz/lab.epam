package com.epam.esm.model.util.mapper;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;

public class TagMapperImpl implements AbstractEntityMapper<TagDto, Tag> {


    @Override
    public Tag toEntity(TagDto tagDto) {
        return Tag.builder()
                .id(tagDto.getId())
                .name(tagDto.getName())
                .build();
    }

    @Override
    public TagDto toDto(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .build();
    }
}
