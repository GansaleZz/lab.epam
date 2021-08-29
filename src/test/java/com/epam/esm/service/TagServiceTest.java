package com.epam.esm.service;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDaoImpl;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagServiceImpl;
import com.epam.esm.service.util.mapper.TagMapperImpl;
import com.epam.esm.util.validation.dto.TagValidationDto;
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
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    @Mock
    private JdbcTemplateTagDaoImpl jdbcTemplateTagDao;

    @Mock
    private TagMapperImpl tagMapper;

    @Mock
    private TagValidationDto tagValidationDto;

    @InjectMocks
    private TagServiceImpl tagService;

    @Test
    void findAllTagsTest() {
        try {
            TagDao tagDao = TagDao.builder().id(1L).name("Test").build();
            TagDto tagDto = TagDto.builder().id(1L).name("Test").build();

            when(tagMapper.toDto(any())).thenReturn(tagDto);
            when(jdbcTemplateTagDao.findAllEntities())
                    .thenReturn(Arrays.asList(tagDao));

            assertEquals(tagDto, tagService.findAllEntities().get(0));
            verify(jdbcTemplateTagDao, times(1))
                    .findAllEntities();
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdExists() {
        try {
            TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
            TagDao tagDao = TagDao.builder().id(1L).name("Test").build();

            when(tagMapper.toDto(any())).thenReturn(tagDto);
            when(jdbcTemplateTagDao.findEntityById(1L))
                    .thenReturn(Optional.of(tagDao));

            assertEquals(tagDto, tagService.findEntityById(1L).get());
            verify(jdbcTemplateTagDao, times(1))
                    .findEntityById(any());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdNotFound() {
        try {
            when(jdbcTemplateTagDao.findEntityById(2L))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> tagService.findEntityById(2L));
            verify(jdbcTemplateTagDao, times(1))
                    .findEntityById(any());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameExists() {
        try {
            TagDto tagDto = TagDto.builder().id(1L).name("Test").build();
            TagDao tagDao = TagDao.builder().id(1L).name("Test").build();

            when(tagMapper.toDto(any())).thenReturn(tagDto);
            when(jdbcTemplateTagDao.findTagByName("Test"))
                    .thenReturn(Optional.of(tagDao));

            assertEquals(tagDto, tagService.findTagByName("Test").get());
            verify(jdbcTemplateTagDao, times(1))
                    .findTagByName(anyString());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameNotFound() {
        try {
            when(jdbcTemplateTagDao.findTagByName(anyString()))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> tagService.findTagByName("Test"));
            verify(jdbcTemplateTagDao, times(1))
                    .findTagByName(anyString());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            TagDto tagDto = TagDto.builder().name("Test").build();
            TagDto tagDtoRes = TagDto.builder().id(1L).name("Test").build();
            TagDao tagDaoRes = TagDao.builder().id(1L).name("Test").build();

            when(tagMapper.toDto(any())).thenReturn(tagDtoRes);
            when(jdbcTemplateTagDao.create(any()))
                    .thenReturn(tagDaoRes);

            assertEquals(tagDtoRes, tagService.create(tagDto));
            verify(jdbcTemplateTagDao, times(1))
                    .create(any());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            TagDto tagDto = TagDto.builder().build();
            TagDao tagDao = TagDao.builder().build();

            when(tagMapper.toDao(any())).thenReturn(tagDao);
            when(jdbcTemplateTagDao.create(tagDao))
                    .thenThrow(EntityBadInputException.class);

            assertThrows(EntityBadInputException.class, () -> tagService.create(tagDto));
            verify(jdbcTemplateTagDao, times(1))
                    .create(any());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            when(jdbcTemplateTagDao.delete(1L))
                    .thenReturn(true);

            assertTrue(tagService.delete(1L));
            verify(jdbcTemplateTagDao, times(1)).delete(1L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            when(jdbcTemplateTagDao.delete(2L))
                    .thenThrow(EntityNotFoundException.class);

            assertThrows(EntityNotFoundException.class, () -> tagService.delete(2L));
            verify(jdbcTemplateTagDao, times(1)).delete(2L);
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}
