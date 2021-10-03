package com.epam.esm.persistence.jpa;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfigJpa.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
public class JpaTagDaoTest {

    @Autowired
    private TagDao jpaTagDao;

    @Autowired
    private UserDao jpaUserDao;

    @Test
    void findAllTags() {
        int sizeAfterInit = 6;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<Tag> listResult = jpaTagDao.findAllTags(paginationFilter);

        assertEquals(sizeAfterInit, listResult.size());
    }

    @Test
    void findTagByIdExists() {
        Long id = 1L;

        Optional<Tag> tagDao = jpaTagDao.findEntityById(id);

        assertTrue(tagDao.isPresent());
        assertEquals(id, tagDao.get().getTagId());
    }

    @Test
    void findTagByIdFailNotFound() {
        Long badId = 150L;

        assertEquals(Optional.empty(), jpaTagDao.findEntityById(badId));
    }

    @Test
    void findMostWidelyUsedTag() {
        String tagName = "TAG_TEST_4";

        Tag tag = jpaTagDao.findMostWidelyUsedTag(jpaUserDao.findUserWithTheHighestOrdersCost());

        assertEquals(tagName, tag.getName());
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

        Tag tagDaoResult = jpaTagDao.createEntity(tagDao);

        assertEquals(7, jpaTagDao.findAllTags(paginationFilter).size());
        assertTrue(jpaTagDao.findEntityById(tagDaoResult.getTagId()).isPresent());
        assertEquals(tagDao.getName(),
                jpaTagDao.findEntityById(tagDaoResult.getTagId()).get().getName());
    }

    @Test
    void createFailBadInput() {
        assertThrows(PersistenceException.class, () -> jpaTagDao.createEntity(new Tag()));
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        int sizeAfterInit = jpaTagDao.findAllTags(paginationFilter).size();

        boolean result = jpaTagDao.deleteEntity(id);

        assertTrue(result);
        assertEquals(sizeAfterInit-1, jpaTagDao.findAllTags(paginationFilter).size());
    }

    @Test
    void deleteFailNotFound() {
        Long id = 1124L;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        int sizeAfterInit = jpaTagDao.findAllTags(paginationFilter).size();

        jpaTagDao.deleteEntity(id);

        assertEquals(sizeAfterInit, jpaTagDao.findAllTags(paginationFilter).size());
    }
}
