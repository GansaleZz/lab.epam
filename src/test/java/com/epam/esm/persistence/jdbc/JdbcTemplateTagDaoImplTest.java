package com.epam.esm.persistence.jdbc;

import com.epam.esm.model.domain.Property;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.web.config.SpringConfig;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcTemplateTagDaoImplTest {
    private JdbcTemplateTagDaoImpl jdbcTemplateTagDao;

    public JdbcTemplateTagDaoImplTest(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Property property = Property.getInstance();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(property.getUSER());
        dataSource.setPassword(property.getPASSWORD());
        dataSource.setUrl(property.getURL()+property.getSCHEME());

        jdbcTemplateTagDao = new JdbcTemplateTagDaoImpl();
        jdbcTemplateTagDao.setDataSource(dataSource);
    }

    @Order(1)
    @Test
    void findAllEntities() {
        assertEquals(1, jdbcTemplateTagDao.findAllEntities().size());
    }

    @Order(2)
    @Test
    void findEntityById() {
        assertFalse(jdbcTemplateTagDao.findEntityById(133).isPresent());
        System.out.println(jdbcTemplateTagDao.findEntityById(133));
    }

    @Order(3)
    @Test
    void create() {
        TagDto tag = TagDto.builder().build();
        tag.setName("Hi");
        assertTrue(jdbcTemplateTagDao.create(tag));
    }

    @Order(4)
    @Test
    void findTagByName(){
        assertTrue(jdbcTemplateTagDao.findTagByName("Hi").isPresent());
    }

    @Order(5)
    @Test
    void delete() {
        int id = jdbcTemplateTagDao.findAllEntities().get(jdbcTemplateTagDao.findAllEntities().size()-1).getId();
        assertTrue(jdbcTemplateTagDao.delete(id));
    }
}