package com.epam.esm.persistence.jdbc;

import com.epam.esm.model.domain.Property;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcTemplateGiftDaoImplTest{
    private JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao;

    public JdbcTemplateGiftDaoImplTest(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        Property property = Property.getInstance();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername(property.getUSER());
        dataSource.setPassword(property.getPASSWORD());
        dataSource.setUrl(property.getURL()+property.getSCHEME());

        jdbcTemplateGiftDao = new JdbcTemplateGiftDaoImpl();
        jdbcTemplateGiftDao.setDataSource(dataSource);
    }

    @Order(1)
    @Test
    void create() {
        GiftCertificateDto giftCertificate = GiftCertificateDto.builder().build();
        giftCertificate.setCreateDate(Calendar.getInstance().getTime());
        giftCertificate.setLastUpdateDate(Calendar.getInstance().getTime());
        giftCertificate.setDuration(1);
        giftCertificate.setPrice(100.0);
        giftCertificate.setName("A");
        giftCertificate.setDescription("A");

        assertTrue(jdbcTemplateGiftDao.create(giftCertificate));
        delete();

        TagDto tag = TagDto.builder().build();
        tag.setName("TagDto for test");
        assertTrue(jdbcTemplateGiftDao.create(giftCertificate, tag));
    }

    @Order(2)
    @Test
    void findAllEntities() {
        System.out.println(jdbcTemplateGiftDao.findAllEntities());
    }

    @Order(3)
    @Test
    void findGiftsByTag(){
        TagDto tag = TagDto.builder().build();
        tag.setName("TagDto for test");
        assertFalse(jdbcTemplateGiftDao.findGiftsByTag(tag).isEmpty());
        tag.setName("bbjbjbjbjjbjb");
        assertTrue(jdbcTemplateGiftDao.findGiftsByTag(tag).isEmpty());
    }

    @Order(4)
    @Test
    void findEntityById() {
        assertFalse(jdbcTemplateGiftDao.findEntityById(123123).isPresent());
    }

    @Order(5)
    @Test
    void update() {
        GiftCertificateDto giftCertificate = jdbcTemplateGiftDao.findEntityById(jdbcTemplateGiftDao.findAllEntities().get(jdbcTemplateGiftDao.findAllEntities().size()-1).getId()).get();
        giftCertificate.setDescription("Aa");
        assertTrue(jdbcTemplateGiftDao.update(giftCertificate));
        giftCertificate.setDescription("A");
        giftCertificate.setName("Aa");
        TagDto tag = TagDto.builder().build();
        tag.setName("A");
        assertTrue(jdbcTemplateGiftDao.update(giftCertificate, tag));
    }

    @Order(6)
    @Test
    void findGiftsByName() {
        assertFalse(jdbcTemplateGiftDao.findGiftsByName("a").isEmpty());
        System.out.println(jdbcTemplateGiftDao.findGiftsByName("a"));
    }

    @Order(7)
    @Test
    void findGiftsByDescription() {
        assertFalse(jdbcTemplateGiftDao.findGiftsByDescription("A").isEmpty());
        System.out.println(jdbcTemplateGiftDao.findGiftsByDescription("A"));
    }

    @Order(8)
    @Test
    void sortGiftsByNameASC() {
        assertEquals("Aa",jdbcTemplateGiftDao.sortGiftsByNameASC().get(0).getName());
    }

    @Order(9)
    @Test
    void sortGiftsByNameDESC() {
        assertEquals("Aa",jdbcTemplateGiftDao.sortGiftsByNameDESC().get(jdbcTemplateGiftDao.findAllEntities().size()-1).getName());
    }

    @Order(10)
    @Test
    void sortGiftsByDescriptionASC() {
        assertEquals("A",jdbcTemplateGiftDao.sortGiftsByDescriptionASC().get(0).getDescription());
    }

    @Order(11)
    @Test
    void sortGiftsByDescriptionDESC() {
        assertEquals("A",jdbcTemplateGiftDao.sortGiftsByDescriptionDESC().get(jdbcTemplateGiftDao.findAllEntities().size()-1).getDescription());
    }

    @Order(12)
    @Test
    void delete() {
        int id = jdbcTemplateGiftDao.findAllEntities().get(jdbcTemplateGiftDao.findAllEntities().size()-1).getId();
        assertTrue(jdbcTemplateGiftDao.delete(id));
    }
}