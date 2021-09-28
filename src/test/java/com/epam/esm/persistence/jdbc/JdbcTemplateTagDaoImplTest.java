package com.epam.esm.persistence.jdbc;

import com.epam.esm.TestConfig;
import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
import com.epam.esm.persistence.util.mapper.TagMapperDb;
import com.epam.esm.util.validation.BaseTagValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class JdbcTemplateTagDaoImplTest {

    private final JdbcTemplateTagDao jdbcTemplateTagDao;

    @Autowired
    public JdbcTemplateTagDaoImplTest(JdbcTemplate jdbcTemplate,
                                      BaseTagValidator<Tag, Long> tagValidation,
                                      TagMapperDb tagMapperDB) {
        this.jdbcTemplateTagDao = new JdbcTemplateTagDao(jdbcTemplate, tagValidation, tagMapperDB);
    }

    @Test
    void findAllTags() {
        int sizeAfterInit = 6;

        List<Tag> listResult = jdbcTemplateTagDao.findAllEntities();

        assertEquals(sizeAfterInit, listResult.size());
    }

    @Test
    void findTagByIdExists() {
        Long id = 1L;

        Optional<Tag> tagDao = jdbcTemplateTagDao.findEntityById(id);

        assertTrue(tagDao.isPresent());
        assertEquals(id, tagDao.get().getId());
    }

    @Test
    void findTagByIdFailNotFound() {
        Long badId = 150L;

        assertEquals(Optional.empty(), jdbcTemplateTagDao.findEntityById(badId));
    }

    @Test
    void findTagByNameExists() {
        String name = "TAG_TEST_2";

        Optional<Tag> tagDao = jdbcTemplateTagDao.findTagByName(name);

        assertTrue(tagDao.isPresent());
        assertEquals(name, tagDao.get().getName());
    }

    @Test
    void findTagByNameFailNotFound() {
        String name = "Random name of tag";

        assertEquals(Optional.empty(), jdbcTemplateTagDao.findTagByName(name));
    }

    @Test
    void createSuccess() {
        Tag tagDao = Tag.builder()
                .name("Tag__")
                .build();

        Tag tagDaoResult = jdbcTemplateTagDao.create(tagDao);

        assertEquals(7, jdbcTemplateTagDao.findAllEntities().size());
        assertTrue(jdbcTemplateTagDao.findEntityById(tagDaoResult.getId()).isPresent());
        assertEquals(tagDao.getName(),
                jdbcTemplateTagDao.findEntityById(tagDaoResult.getId()).get().getName());
    }

    @Test
    void createFailBadInput() {
      assertThrows(EntityBadInputException.class, () -> jdbcTemplateTagDao.create(new Tag()));
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        int sizeAfterInit = jdbcTemplateTagDao.findAllEntities().size();

        boolean result = jdbcTemplateTagDao.delete(id);

        assertTrue(result);
        assertEquals(sizeAfterInit-1, jdbcTemplateTagDao.findAllEntities().size());
    }

    @Test
    void deleteFailNotFound() {
        Long id = 1124L;

        assertFalse(jdbcTemplateTagDao.delete(id));
    }
}