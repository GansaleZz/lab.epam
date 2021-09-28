package com.epam.esm.service.tag;

import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.util.validation.BaseTagValidator;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private static final String NOT_FOUND_BY_ID = "Requested tag not found (id = %s)";
    private static final String NOT_FOUND_BY_NAME = "Requested tag not found (name = %s)";
    private final JdbcTemplateTagDao jdbcTemplateTagDao;
    private final AbstractEntityMapper<TagDto, Tag> tagMapper;
    private final BaseTagValidator<TagDto, Long> tagValidation;

    @Autowired
    public TagServiceImpl(JdbcTemplateTagDao jdbcTemplateTagDao,
                          AbstractEntityMapper<TagDto, Tag> tagMapper,
                          BaseTagValidator<TagDto, Long> tagValidation) {
        this.jdbcTemplateTagDao = jdbcTemplateTagDao;
        this.tagMapper = tagMapper;
        this.tagValidation = tagValidation;
    }

    @Override
    public List<TagDto> findAllTags() {
        return jdbcTemplateTagDao.findAllEntities()
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagById(Long id) {
         return jdbcTemplateTagDao.findEntityById(id)
                 .map(tagMapper::toDto)
                 .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_ID, id)));
    }

    @Transactional
    @Override
    public TagDto create(TagDto tagDto) {
        tagValidation.onBeforeInsert(tagDto);
        return tagMapper.toDto(jdbcTemplateTagDao
                .create(tagMapper.toEntity(tagDto)));
    }

    @Override
    public boolean delete(Long id) {
        if (!jdbcTemplateTagDao.delete(id)) {
            throw new EntityNotFoundException(String.format(NOT_FOUND_BY_ID, id));
        } else {
            return true;
        }
    }

    @Override
    public TagDto findTagByName(String name) {
        return jdbcTemplateTagDao.findTagByName(name)
                    .map(tagMapper::toDto)
                    .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_NAME, name)));
    }
}
