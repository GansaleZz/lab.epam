package com.epam.esm.persistence.jpa;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfigJpa.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
public class JpaUserDaoTest {

    @Autowired
    private UserDao jpaUserDao;

    @Test
    void findAllUsers() {
        int size = 4;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        int actualSize = jpaUserDao.findAllUsers(paginationFilter).size();

        assertEquals(size, actualSize);
    }

    @Test
    void findUserByIdExists() {
        long id = 1L;

        assertTrue(jpaUserDao.findUserById(id).isPresent());
    }

    @Test
    void findUserByIdNotFound() {
        Long id = 10012L;

        assertFalse(jpaUserDao.findUserById(id).isPresent());
    }

    @Test
    void findUserWithTheHighestOrdersCost() {
        long expectedId = 1L;

        long actualId = jpaUserDao.findUserWithTheHighestOrdersCost();

        assertEquals(expectedId, actualId);
    }
}
