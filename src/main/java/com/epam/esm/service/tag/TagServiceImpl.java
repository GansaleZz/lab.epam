package com.epam.esm.service.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDaoImpl;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.util.validation.BeforeTagValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    
    private final JdbcTemplateTagDaoImpl jdbcTemplateTagDao;
    private final AbstractEntityMapper<TagDto, TagDao> tagMapper;
    private final BeforeTagValidation<TagDto, Long> tagValidation;

    @Autowired
    public TagServiceImpl(JdbcTemplateTagDaoImpl jdbcTemplateTagDao,
                          AbstractEntityMapper<TagDto, TagDao> tagMapper,
                          BeforeTagValidation<TagDto, Long> tagValidation) {
        this.jdbcTemplateTagDao = jdbcTemplateTagDao;
        this.tagMapper = tagMapper;
        this.tagValidation = tagValidation;
    }

    @Override
    public List<TagDto> findAllEntities() {
        return jdbcTemplateTagDao.findAllEntities()
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TagDto> findEntityById(Long id) {
         Optional<TagDto> tagDto = jdbcTemplateTagDao.findEntityById(id).map(tagMapper::toDto);
         tagValidation.onBeforeFindEntity(tagDto, id);
         return tagDto;
    }

    @Override
    @Transactional(rollbackFor = EntityBadInputException.class)
    public TagDto create(TagDto tagDto) {
        tagValidation.onBeforeInsert(tagDto);
        return tagMapper.toDto(jdbcTemplateTagDao
                .create(tagMapper.toDao(tagDto)));
    }

    @Override
    public boolean delete(Long id) {
        tagValidation.onBeforeDelete(jdbcTemplateTagDao.delete(id), id);
        return true;
    }

    @Override
    public Optional<TagDto> findTagByName(String name) {
        Optional<TagDto> tagDto = jdbcTemplateTagDao.findTagByName(name).map(tagMapper::toDto);
        tagValidation.onBeforeFindEntity(tagDto, name);
        return tagDto;
    }
}
