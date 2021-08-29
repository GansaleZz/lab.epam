package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.service.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagMapperImpl implements AbstractEntityMapper<TagDto, TagDao> {

    @Override
    public TagDao toDao(TagDto tagDto) {
        return TagDao.builder()
                .id(tagDto.getId())
                .name(tagDto.getName())
                .build();
    }

    @Override
    public TagDto toDto(TagDao tagDao) {
        return TagDto.builder()
                .id(tagDao.getId())
                .name(tagDao.getName())
                .build();
    }
}
