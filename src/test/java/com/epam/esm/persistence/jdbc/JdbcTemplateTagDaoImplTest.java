package com.epam.esm.persistence.jdbc;

import com.epam.esm.TestConfigJdbc;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfigJdbc.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJdbcTest
class JdbcTemplateTagDaoImplTest {

    @Autowired
    private TagDao jdbcTagDao;

    @Test
    void findAllTags() {
        int sizeAfterInit = 6;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<Tag> listResult = jdbcTagDao.findAllTags(paginationFilter);

        assertEquals(sizeAfterInit, listResult.size());
    }

    @Test
    void findTagByIdExists() {
        Long id = 1L;

        Optional<Tag> tagDao = jdbcTagDao.findEntityById(id);

        assertTrue(tagDao.isPresent());
        assertEquals(id, tagDao.get().getTagId());
    }

    @Test
    void findTagByIdFailNotFound() {
        Long badId = 150L;

        assertEquals(Optional.empty(), jdbcTagDao.findEntityById(badId));
    }

    @Test
    void createSuccess() {
        Tag tagDao = Tag.builder()
                .name("Tag__")
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        Tag tagDaoResult = jdbcTagDao.createEntity(tagDao);

        assertEquals(7, jdbcTagDao.findAllTags(paginationFilter).size());
        assertTrue(jdbcTagDao.findEntityById(tagDaoResult.getTagId()).isPresent());
        assertEquals(tagDao.getName(),
                jdbcTagDao.findEntityById(tagDaoResult.getTagId()).get().getName());
    }

    @Test
    void createFailBadInput() {
      assertThrows(EntityBadInputException.class, () -> jdbcTagDao.createEntity(new Tag()));
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        int sizeAfterInit = jdbcTagDao.findAllTags(paginationFilter).size();

        boolean result = jdbcTagDao.deleteEntity(id);

        assertTrue(result);
        assertEquals(sizeAfterInit-1, jdbcTagDao.findAllTags(paginationFilter).size());
    }

    @Test
    void deleteFailNotFound() {
        Long id = 1124L;

        assertFalse(jdbcTagDao.deleteEntity(id));
    }
}