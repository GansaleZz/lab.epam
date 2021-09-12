package com.epam.esm.service.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private static final String NOT_FOUND_BY_ID = "Requested tag not found (id = %s)";

    @Autowired
    private TagDao tagDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AbstractEntityMapper<TagDto, Tag> tagMapper;

    @Override
    public List<TagDto> findAllTags() {
        return tagDao.findAllEntities()
                .stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TagDto findTagById(Long id) {
         return tagDao.findEntityById(id)
                 .map(tagMapper::toDto)
                 .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_BY_ID,
                         id)));
    }

    @Override
    public TagDto findMostWidelyUsedTag() {
        return tagMapper.toDto(tagDao
                .findMostWidelyUsedTag(userDao
                        .findUserWithTheHighestCost()));
    }

    @Override
    public TagDto create(TagDto tagDto) {
        return tagMapper.toDto(tagDao
                .create(tagMapper.toEntity(tagDto)));
    }

    @Override
    public boolean delete(Long id) {
        if (!tagDao.delete(id)) {
            throw new EntityNotFoundException(String.format(NOT_FOUND_BY_ID, id));
        } else {
            return true;
        }
    }
}
