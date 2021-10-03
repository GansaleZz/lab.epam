package com.epam.esm.service.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final UserDao userDao;
    private final AbstractEntityMapper<TagDto, Tag> tagMapper;

    @Autowired
    public TagServiceImpl(TagDao tagDao, UserDao userDao, AbstractEntityMapper<TagDto, Tag> tagMapper) {
        this.tagDao = tagDao;
        this.userDao = userDao;
        this.tagMapper = tagMapper;
    }

    @Override
    public List<TagDto> findAllTags(PageFilter paginationFilter) {
        return tagDao.findAllTags(paginationFilter)
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagById(Long tagId) {
         return tagDao.findEntityById(tagId)
                 .map(tagMapper::toDto)
                 .orElseThrow(() -> new EntityNotFoundException(tagId.toString()));
    }

    @Override
    public TagDto findMostWidelyUsedTag() {
        return tagMapper.toDto(tagDao
                .findMostWidelyUsedTag(userDao
                        .findUserWithTheHighestOrdersCost()));
    }

    @Override
    public TagDto createTag(TagDto tagDto) {
        return tagMapper.toDto(tagDao
                .createEntity(tagMapper.toEntity(tagDto)));
    }

    @Override
    public boolean deleteTag(Long tagId) {
        return tagDao.deleteEntity(tagId);
    }
}
