package com.epam.esm.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private TagDao tagDao;

    @Mock
    private AbstractEntityMapper<TagDto, Tag> tagMapper;

    @Mock
    private UserDao userDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findAllTagsTest() {
        Tag tag = Tag.builder().tagId(1L).name("Test").build();
        TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
        int paginationItems = 1000;
        PageFilter pageFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        when(tagMapper.toDto(any())).thenReturn(tagDto);
        when(tagDao.findAllTags(pageFilter))
                .thenReturn(Collections.singletonList(tag));

        List<TagDto> actualList = tagService.findAllTags(pageFilter);

        assertEquals(tagDto, actualList.get(0));
        verify(tagDao, times(1))
                .findAllTags(pageFilter);
    }

    @Test
    void findTagByIdExists() {
        TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
        Tag tag = Tag.builder().tagId(1L).name("Test").build();
        when(tagMapper.toDto(any())).thenReturn(tagDto);
        when(tagDao.findEntityById(1L))
                .thenReturn(Optional.of(tag));

        TagDto actualTagDto = tagService.findTagById(1L);

        assertEquals(tagDto, actualTagDto);
        verify(tagDao, times(1))
                .findEntityById(any());
    }

    @Test
    void findTagByIdNotFound() {
        when(tagDao.findEntityById(2L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> tagService.findTagById(2L));
        verify(tagDao, times(1))
                .findEntityById(any());
    }

    @Test
    void findMostWidelyUsedTag() {
        Tag tag = Tag.builder()
                .tagId(1L)
                .name("Result tag")
                .build();
        TagDto tagDtoResult = TagDto.builder()
                .id(1L)
                .name("Result tag")
                .build();
        when(userDao.findUserWithTheHighestOrdersCost())
                .thenReturn(1L);
        when(tagDao.findMostWidelyUsedTag(1L))
                .thenReturn(tag);
        when(tagMapper.toDto(any()))
                .thenReturn(tagDtoResult);

        TagDto tagDto = tagService.findMostWidelyUsedTag();

        assertEquals(tagDtoResult, tagDto);
        verify(tagDao, times(1))
                .findMostWidelyUsedTag(any());
        verify(userDao, times(1))
                .findUserWithTheHighestOrdersCost();
    }

    @Test
    void createSuccess() {
        TagDto tagDto = TagDto.builder().name("Test").build();
        TagDto tagDtoResult = TagDto.builder().id(1L).name("Test").build();
        Tag tagResult = Tag.builder().tagId(1L).name("Test").build();
        when(tagMapper.toDto(any())).thenReturn(tagDtoResult);
        when(tagDao.createEntity(any()))
                .thenReturn(tagResult);

        TagDto actualTagDto = tagService.createTag(tagDto);

        assertEquals(tagDtoResult, actualTagDto);
        verify(tagDao, times(1))
                .createEntity(any());
    }

    @Test
    void createFailBadInput() {
        TagDto tagDto = TagDto.builder().build();
        Tag tag = Tag.builder().build();
        when(tagMapper.toEntity(any())).thenReturn(tag);
        when(tagDao.createEntity(tag))
                .thenThrow(EntityBadInputException.class);

        assertThrows(EntityBadInputException.class, () -> tagService.createTag(tagDto));
        verify(tagDao, times(1))
                .createEntity(any());

    }

    @Test
    void deleteSuccess() {
        when(tagDao.deleteEntity(1L))
                .thenReturn(true);

        boolean result = tagService.deleteTag(1L);

        assertTrue(result);
        verify(tagDao, times(1)).deleteEntity(1L);
    }
}
