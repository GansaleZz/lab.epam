package com.epam.esm.service;

import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagServiceImpl;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.util.validation.dto.TagDtoValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private JdbcTemplateTagDao jdbcTemplateTagDao;

    @Mock
    private AbstractEntityMapper<TagDto, Tag> tagMapper;

    @Mock
    private TagDtoValidator tagValidationDto;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findAllTagsTest() {
        Tag tagDao = Tag.builder().id(1L).name("Test").build();
        TagDto tagDto = TagDto.builder().id(1L).name("Test").build();

        when(tagMapper.toDto(any())).thenReturn(tagDto);
        when(jdbcTemplateTagDao.findAllEntities())
                .thenReturn(Arrays.asList(tagDao));

        assertEquals(tagDto, tagService.findAllTags().get(0));
        verify(jdbcTemplateTagDao, times(1))
                .findAllEntities();
    }

    @Test
    void findTagByIdExists() {
        TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
        Tag tagDao = Tag.builder().id(1L).name("Test").build();

        when(tagMapper.toDto(any())).thenReturn(tagDto);
        when(jdbcTemplateTagDao.findEntityById(1L))
                .thenReturn(Optional.of(tagDao));

        assertEquals(tagDto, tagService.findTagById(1L));
        verify(jdbcTemplateTagDao, times(1))
                .findEntityById(any());
    }

    @Test
    void findTagByIdNotFound() {
        when(jdbcTemplateTagDao.findEntityById(2L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> tagService.findTagById(2L));
        verify(jdbcTemplateTagDao, times(1))
                .findEntityById(any());
    }

    @Test
    void findTagByNameExists() {
        TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
        Tag tagDao = Tag.builder().id(1L).name("Test").build();

        when(tagMapper.toDto(any())).thenReturn(tagDto);
        when(jdbcTemplateTagDao.findTagByName("Test"))
                .thenReturn(Optional.of(tagDao));

        assertEquals(tagDto, tagService.findTagByName("Test"));
        verify(jdbcTemplateTagDao, times(1))
                .findTagByName(anyString());
    }

    @Test
    void findTagByNameNotFound() {
        when(jdbcTemplateTagDao.findTagByName(anyString()))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> tagService.findTagByName("Test"));
        verify(jdbcTemplateTagDao, times(1))
                .findTagByName(anyString());
    }

    @Test
    void createSuccess() {
        TagDto tagDto = TagDto.builder().name("Test").build();
        TagDto tagDtoRes = TagDto.builder().id(1L).name("Test").build();
        Tag tagDaoRes = Tag.builder().id(1L).name("Test").build();

        when(tagMapper.toDto(any())).thenReturn(tagDtoRes);
        when(jdbcTemplateTagDao.create(any()))
                .thenReturn(tagDaoRes);

        assertEquals(tagDtoRes, tagService.create(tagDto));
        verify(jdbcTemplateTagDao, times(1))
                .create(any());
    }

    @Test
    void createFailBadInput() {
        TagDto tagDto = TagDto.builder().build();
        Tag tagDao = Tag.builder().build();

        when(tagMapper.toEntity(any())).thenReturn(tagDao);
        when(jdbcTemplateTagDao.create(tagDao))
                .thenThrow(EntityBadInputException.class);

        assertThrows(EntityBadInputException.class, () -> tagService.create(tagDto));
        verify(jdbcTemplateTagDao, times(1))
                .create(any());

    }

    @Test
    void deleteSuccess() {
        when(jdbcTemplateTagDao.delete(1L))
                .thenReturn(true);

        assertTrue(tagService.delete(1L));
        verify(jdbcTemplateTagDao, times(1)).delete(1L);
    }

    @Test
    void deleteFailNotFound() {
        when(jdbcTemplateTagDao.delete(2L))
                .thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class, () -> tagService.delete(2L));
        verify(jdbcTemplateTagDao, times(1)).delete(2L);
    }
}
