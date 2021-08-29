package com.epam.esm.persistence.jdbc;

import com.epam.esm.TestConfig;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDaoImpl;
import com.epam.esm.util.validation.BeforeTagValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
@Sql({"classpath:sql/db-schema.sql", "classpath:sql/db-test-data.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class JdbcTemplateTagDaoImplTest {

    private final JdbcTemplateTagDaoImpl jdbcTemplateTagDao;

    @Autowired
    public JdbcTemplateTagDaoImplTest(JdbcTemplate jdbcTemplate,
                                      BeforeTagValidation<TagDao, Long> tagValidation) {
        this.jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl(jdbcTemplate, tagValidation);
    }

    @Test
    void findAllTags() {
        try {
            int sizeAfterInit = 6;

            List<TagDao> listResult = jdbcTemplateTagDao.findAllEntities();

            assertEquals(sizeAfterInit, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdExists() {
        try {
            Long id = 1L;

            Optional<TagDao> tagDao = jdbcTemplateTagDao.findEntityById(id);

            assertTrue(tagDao.isPresent());
            assertEquals(id, tagDao.get().getId());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByIdFailNotFound() {
        try {
            Long badId = 150L;

            assertThrows(EntityNotFoundException.class,() -> jdbcTemplateTagDao.findEntityById(badId));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameExists() {
        try {
            String name = "TAG_TEST_2";

            Optional<TagDao> tagDao = jdbcTemplateTagDao.findTagByName(name);

            assertTrue(tagDao.isPresent());
            assertEquals(name, tagDao.get().getName());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findTagByNameFailNotFound() {
        try {
            String name = "Random name of tag";

            assertThrows(EntityNotFoundException.class, () -> jdbcTemplateTagDao.findTagByName(name));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            TagDao tagDao = TagDao.builder()
                    .name("Tag__")
                    .build();

            TagDao tagDaoResult = jdbcTemplateTagDao.create(tagDao);

            assertEquals(7, jdbcTemplateTagDao.findAllEntities().size());
            assertTrue(jdbcTemplateTagDao.findEntityById(tagDaoResult.getId()).isPresent());
            assertEquals(tagDao.getName(), jdbcTemplateTagDao.findEntityById(tagDaoResult.getId()).get().getName());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            assertThrows(EntityBadInputException.class, () -> jdbcTemplateTagDao.create(new TagDao()));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            Long id = 1L;
            int sizeAfterInit = jdbcTemplateTagDao.findAllEntities().size();

            boolean result = jdbcTemplateTagDao.delete(id);

            assertTrue(result);
            assertEquals(sizeAfterInit-1, jdbcTemplateTagDao.findAllEntities().size());
            assertThrows(EntityNotFoundException.class, () -> jdbcTemplateTagDao.findEntityById(id));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            Long id = 1124L;

            assertThrows(EntityNotFoundException.class, () -> jdbcTemplateTagDao.delete(id));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}