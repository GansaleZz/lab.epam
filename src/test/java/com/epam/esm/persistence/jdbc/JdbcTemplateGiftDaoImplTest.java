package com.epam.esm.persistence.jdbc;

import com.epam.esm.TestConfig;
import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDao;
import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDaoImpl;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.util.validation.BeforeGiftValidation;
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
class JdbcTemplateGiftDaoImplTest {

    private final JdbcTemplateGiftDao jdbcTemplateGiftDao;

    @Autowired
    public JdbcTemplateGiftDaoImplTest(JdbcTemplate jdbcTemplate,
                                   BeforeGiftValidation<GiftCertificateDao, Long> giftValidation) {
        jdbcTemplateGiftDao = new JdbcTemplateGiftDaoImpl(jdbcTemplate, giftValidation);
    }

    @Test
    void findAllGiftsWithoutSearchFilterParams() {
        try {
            int sizeAfterInit = 4;

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter());

            assertEquals(sizeAfterInit, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithTagNotExists() {
        try {
            String tagName = "Hey hey hey, Test!";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder().tag(tagName).build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(0, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithTagExists() {
        try {
            String tagName = "TAG_TEST_1";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder().tag(tagName).build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(1, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByNamePartExists() {
        try {
            String namePart1 = "TEST";
            String namePart2 = "2";
            GiftSearchFilter giftSearchFilter1 = GiftSearchFilter.builder()
                    .giftName(namePart1)
                    .build();
            GiftSearchFilter giftSearchFilter2 = GiftSearchFilter.builder()
                    .giftName(namePart2)
                    .build();

            List<GiftCertificateDao> listFirstResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter1);
            List<GiftCertificateDao> listSecondResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter2);

            assertEquals(4, listFirstResult.size());
            listFirstResult.forEach(giftCertificateDao ->  assertTrue(giftCertificateDao.getName().contains(namePart1)));
            assertEquals(1, listSecondResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByNamePartNotExists() {
        try {
            String namePart = "test";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftName(namePart)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(0, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByDescriptionPartExists() {
        try {
            String descriptionPart = "_3";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftDescription(descriptionPart)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(1, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByNameDescriptionNotExists() {
        try {
            String descriptionPart = "_3!!";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftDescription(descriptionPart)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(0, listResult.size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByNameAscOrder() {
        try {
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftsByNameOrder(QueryOrder.ASC)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals("TEST1",listResult.get(0).getName());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsByDateDescOrder() {
        try {
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftsByNameOrder(QueryOrder.DESC)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals("TEST1",listResult.get(listResult.size()-1 ).getName());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findAllGiftsWithAllParamsExists() {
        try {
            String tagName = "TAG_TEST_2";
            String namePart = "TEST";
            String descriptionPart = "_";
            GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
                    .giftName(namePart)
                    .tag(tagName)
                    .giftDescription(descriptionPart)
                    .giftsByNameOrder(QueryOrder.ASC)
                    .giftsByDateOrder(QueryOrder.DESC)
                    .build();

            List<GiftCertificateDao> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);

            assertEquals(2, listResult.size());
            listResult.forEach(giftCertificateDao ->
                assertEquals(tagName, giftCertificateDao.getTags().get(0).getName())
            );
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }


    @Test
    void findGiftByIdExists() {
        try {
            Long id = 1L;

            Optional<GiftCertificateDao> giftCertificateDao = jdbcTemplateGiftDao.findEntityById(id);

            assertTrue(giftCertificateDao.isPresent());
            assertEquals(id, giftCertificateDao.get().getId());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void findGiftByIdFailNotFound() {
        try {
            Long badId = 150L;

            assertThrows(EntityNotFoundException.class,() -> jdbcTemplateGiftDao.findEntityById(badId));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createSuccess() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao
                    .builder()
                    .name("Simple test")
                    .description("Only for test")
                    .duration(1)
                    .price(1.0)
                    .build();

            Long id = jdbcTemplateGiftDao.create(giftCertificateDao).getId();

            assertEquals(giftCertificateDao, jdbcTemplateGiftDao.findEntityById(id).get());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void createFailBadInput() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .name("Hey Hey")
                    .build();

            assertThrows(EntityBadInputException.class, () -> jdbcTemplateGiftDao.create(giftCertificateDao));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateSuccess() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .id(1L)
                    .name("Updated name for test")
                    .build();

            jdbcTemplateGiftDao.update(giftCertificateDao);

            assertEquals(giftCertificateDao.getName(), jdbcTemplateGiftDao
                    .findEntityById(giftCertificateDao.getId())
                    .get()
                    .getName());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void updateFailNotFound() {
        try {
            GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                    .id(5L)
                    .name("Updated name for test")
                    .build();

            assertThrows(EntityNotFoundException.class, () -> jdbcTemplateGiftDao.update(giftCertificateDao));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteSuccess() {
        try {
            Long id = 1L;
            int sizeBefore = jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter()).size();

            boolean res = jdbcTemplateGiftDao.delete(id);

            assertTrue(res);
            assertEquals(sizeBefore-1, jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter()).size());
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }

    @Test
    void deleteFailNotFound() {
        try {
            Long id = 15175515L;

            assertThrows(EntityNotFoundException.class, () -> jdbcTemplateGiftDao.delete(id));
        } catch (Exception e) {
            fail("Unexpected exception", e);
        }
    }
}