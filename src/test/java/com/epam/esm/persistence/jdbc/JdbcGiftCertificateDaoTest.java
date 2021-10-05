package com.epam.esm.persistence.jdbc;

import com.epam.esm.TestConfigJdbc;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.jdbc.giftCertificate.JdbcTemplateGiftCertificateDao;
import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
import com.epam.esm.persistence.jdbc.util.mapper.GiftMapperDb;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.persistence.jdbc.util.validation.BaseGiftValidator;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
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
class JdbcGiftCertificateDaoTest {

    private final GiftCertificateDao jdbcTemplateGiftDao;

    @Autowired
    public JdbcGiftCertificateDaoTest(JdbcTemplate jdbcTemplate,
                                      BaseGiftValidator<GiftCertificate, Long> giftValidation,
                                      GiftMapperDb giftMapperDB,
                                      JdbcTemplateTagDao jdbcTemplateTagDao) {
        jdbcTemplateGiftDao = new JdbcTemplateGiftCertificateDao(jdbcTemplate, giftValidation,
                giftMapperDB, jdbcTemplateTagDao);
    }

    @Test
    void findAllGiftCertificatesWithoutSearchFilterParams() {
        int sizeAfterInit = 4;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter);

        assertEquals(sizeAfterInit, listResult.size());
    }

    @Test
    void findAllGiftCertificatesWithTagNotExists() {
        String tagName = "Hey hey hey, Test!";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .tags(Collections.singletonList(tagName))
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftCertificatesWithTagExists() {
        String tagName = "TAG_TEST_1";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .tags(Collections.singletonList(tagName))
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(1, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNamePartExists() {
        String namePart = "TEST";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificateName(namePart)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listFirstResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(4, listFirstResult.size());
        listFirstResult.forEach(giftCertificateDao ->  assertTrue(giftCertificateDao.getName().contains(namePart)));
    }

    @Test
    void findAllGiftCertificatesByNamePartNotExists() {
        String namePart = "test";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificateName(namePart)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByDescriptionPartExists() {
        String descriptionPart = "_3";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificateDescription(descriptionPart)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(1, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNameDescriptionNotExists() {
        String descriptionPart = "_3!!";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificateDescription(descriptionPart)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNameAscOrder() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificatesByNameOrder(QueryOrder.ASC)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals("TEST1",listResult.get(0).getName());
    }

    @Test
    void findAllGiftCertificatesByDateDescOrder() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificatesByNameOrder(QueryOrder.DESC)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals("TEST1",listResult.get(listResult.size()-1 ).getName());
    }

    @Test
    void findAllGiftCertificatesWithAllParamsExists() {
        String tagName = "TAG_TEST_2";
        String namePart = "TEST";
        String descriptionPart = "_";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftCertificateName(namePart)
                .tags(Collections.singletonList(tagName))
                .giftCertificateDescription(descriptionPart)
                .giftCertificatesByNameOrder(QueryOrder.ASC)
                .giftCertificatesByDateOrder(QueryOrder.DESC)
                .build();
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jdbcTemplateGiftDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(2, listResult.size());
        listResult.forEach(giftCertificateDao ->
                assertEquals(tagName, giftCertificateDao.getTags().get(0).getName())
        );
    }

    @Test
    void findGiftCertificateByIdExists() {
        Long id = 1L;

        Optional<GiftCertificate> giftCertificateDao = jdbcTemplateGiftDao.findEntityById(id);

        assertTrue(giftCertificateDao.isPresent());
        assertEquals(id, giftCertificateDao.get().getGiftId());
    }

    @Test
    void findGiftCertificateByIdFailNotFound() {
        Long badId = 150L;

        assertEquals(Optional.empty(), jdbcTemplateGiftDao.findEntityById(badId));
    }

    @Test
    void createSuccess() {
        BigDecimal price = BigDecimal.valueOf(1.0);
        GiftCertificate giftCertificateDao = GiftCertificate
                .builder()
                .name("Simple test")
                .description("Only for test")
                .duration(Duration.ofDays(1))
                .price(price)
                .build();

        GiftCertificate giftCertificate = jdbcTemplateGiftDao.createEntity(giftCertificateDao);
        giftCertificateDao.setCreateDate(giftCertificate.getCreateDate().toLocalDate().atStartOfDay());
        giftCertificateDao.setLastUpdateDate(giftCertificate.getLastUpdateDate().toLocalDate().atStartOfDay());

        assertEquals(giftCertificateDao, jdbcTemplateGiftDao.findEntityById(giftCertificate.getGiftId()).get());
    }

    @Test
    void createFailBadInput() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Hey Hey")
                .build();

        assertThrows(EntityBadInputException.class, () -> jdbcTemplateGiftDao.createEntity(giftCertificateDao));
    }

    @Test
    void updateSuccess() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .giftId(1L)
                .name("Updated name for test")
                .build();

        jdbcTemplateGiftDao.updateGiftCertificate(giftCertificateDao);

        assertEquals(giftCertificateDao.getName(), jdbcTemplateGiftDao
                .findEntityById(giftCertificateDao.getGiftId())
                .get()
                .getName());
    }

    @Test
    void updateFailNotFound() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .giftId(5L)
                .name("Updated name for test")
                .build();

        assertThrows(EntityNotFoundException.class, () -> jdbcTemplateGiftDao.updateGiftCertificate(giftCertificateDao));
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        int paginationItems = 1000;
        PageFilter paginationFilter = PageFilter.builder()
                .items(paginationItems)
                .build();
        int sizeBefore = jdbcTemplateGiftDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter).size();

        boolean res = jdbcTemplateGiftDao.deleteEntity(id);

        assertTrue(res);
        assertEquals(sizeBefore-1, jdbcTemplateGiftDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter).size());
    }

    @Test
    void deleteFailNotFound() {
        Long id = 15175515L;

        assertFalse(jdbcTemplateGiftDao.deleteEntity(id));
    }
}