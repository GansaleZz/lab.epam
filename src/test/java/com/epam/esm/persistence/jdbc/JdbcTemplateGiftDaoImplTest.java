//package com.epam.esm.persistence.jdbc;
//
//import com.epam.esm.TestConfig;
//import com.epam.esm.persistence.entity.GiftCertificate;
//import com.epam.esm.persistence.dao.GiftDao;
//import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDao;
//import com.epam.esm.persistence.jdbc.tag.JdbcTemplateTagDao;
//import com.epam.esm.persistence.jdbc.util.mapper.GiftMapperDb;
//import com.epam.esm.persistence.util.search.GiftSearchFilter;
//import com.epam.esm.persistence.util.search.QueryOrder;
//import com.epam.esm.persistence.jdbc.util.validation.BaseGiftValidator;
//import com.epam.esm.web.exception.EntityBadInputException;
//import com.epam.esm.web.exception.EntityNotFoundException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.context.support.AnnotationConfigContextLoader;
//
//import java.math.BigDecimal;
//import java.time.Duration;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = { TestConfig.class }, loader = AnnotationConfigContextLoader.class)
//@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//class JdbcTemplateGiftDaoImplTest {
//
//    private final GiftDao jdbcTemplateGiftDao;
//
//    @Autowired
//    public JdbcTemplateGiftDaoImplTest(JdbcTemplate jdbcTemplate,
//                                       BaseGiftValidator<GiftCertificate, Long> giftValidation,
//                                       GiftMapperDb giftMapperDB,
//                                       JdbcTemplateTagDao jdbcTemplateTagDao) {
//        jdbcTemplateGiftDao = new JdbcTemplateGiftDao(jdbcTemplate, giftValidation,
//                giftMapperDB, jdbcTemplateTagDao);
//    }
//
//    @Test
//    void findAllGiftsWithoutSearchFilterParams() {
//        int sizeAfterInit = 4;
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter());
//
//        assertEquals(sizeAfterInit, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsWithTagNotExists() {
//        String tagName = "Hey hey hey, Test!";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder().tag(tagName).build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(0, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsWithTagExists() {
//        String tagName = "TAG_TEST_1";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder().tag(tagName).build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(1, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsByNamePartExists() {
//        String namePart = "TEST";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftName(namePart)
//                .build();
//
//        List<GiftCertificate> listFirstResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(4, listFirstResult.size());
//        listFirstResult.forEach(giftCertificateDao ->  assertTrue(giftCertificateDao.getName().contains(namePart)));
//    }
//
//    @Test
//    void findAllGiftsByNamePartNotExists() {
//        String namePart = "test";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftName(namePart)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(0, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsByDescriptionPartExists() {
//        String descriptionPart = "_3";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftDescription(descriptionPart)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(1, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsByNameDescriptionNotExists() {
//        String descriptionPart = "_3!!";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftDescription(descriptionPart)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(0, listResult.size());
//    }
//
//    @Test
//    void findAllGiftsByNameAscOrder() {
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftsByNameOrder(QueryOrder.ASC)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals("TEST1",listResult.get(0).getName());
//    }
//
//    @Test
//    void findAllGiftsByDateDescOrder() {
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftsByNameOrder(QueryOrder.DESC)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals("TEST1",listResult.get(listResult.size()-1 ).getName());
//    }
//
//    @Test
//    void findAllGiftsWithAllParamsExists() {
//        String tagName = "TAG_TEST_2";
//        String namePart = "TEST";
//        String descriptionPart = "_";
//        GiftSearchFilter giftSearchFilter = GiftSearchFilter.builder()
//                .giftName(namePart)
//                .tag(tagName)
//                .giftDescription(descriptionPart)
//                .giftsByNameOrder(QueryOrder.ASC)
//                .giftsByDateOrder(QueryOrder.DESC)
//                .build();
//
//        List<GiftCertificate> listResult = jdbcTemplateGiftDao.findAllEntities(giftSearchFilter);
//
//        assertEquals(2, listResult.size());
//        listResult.forEach(giftCertificateDao ->
//                assertEquals(tagName, giftCertificateDao.getTags().get(0).getName())
//        );
//    }
//
//
//    @Test
//    void findGiftByIdExists() {
//        Long id = 1L;
//
//        Optional<GiftCertificate> giftCertificateDao = jdbcTemplateGiftDao.findEntityById(id);
//
//        assertTrue(giftCertificateDao.isPresent());
//        assertEquals(id, giftCertificateDao.get().getGiftId());
//    }
//
//    @Test
//    void findGiftByIdFailNotFound() {
//            Long badId = 150L;
//
//            assertEquals(Optional.empty(), jdbcTemplateGiftDao.findEntityById(badId));
//    }
//
//    @Test
//    void createSuccess() {
//            GiftCertificate giftCertificateDao = GiftCertificate
//                    .builder()
//                    .name("Simple test")
//                    .description("Only for test")
//                    .duration(Duration.ofDays(1))
//                    .price(BigDecimal.ONE)
//                    .build();
//
//            GiftCertificate giftCertificate = jdbcTemplateGiftDao.create(giftCertificateDao);
//            giftCertificateDao.setCreateDate(giftCertificate.getCreateDate().toLocalDate().atStartOfDay());
//            giftCertificateDao.setLastUpdateDate(giftCertificate.getLastUpdateDate().toLocalDate().atStartOfDay());
//
//            assertEquals(giftCertificateDao, jdbcTemplateGiftDao.findEntityById(giftCertificate.getGiftId()).get());
//    }
//
//    @Test
//    void createFailBadInput() {
//        GiftCertificate giftCertificateDao = GiftCertificate.builder()
//                .name("Hey Hey")
//                .build();
//
//        assertThrows(EntityBadInputException.class, () -> jdbcTemplateGiftDao.create(giftCertificateDao));
//    }
//
//    @Test
//    void updateSuccess() {
//        GiftCertificate giftCertificateDao = GiftCertificate.builder()
//                .id(1L)
//                .name("Updated name for test")
//                .build();
//
//        jdbcTemplateGiftDao.update(giftCertificateDao);
//
//        assertEquals(giftCertificateDao.getName(), jdbcTemplateGiftDao
//                .findEntityById(giftCertificateDao.getGiftId())
//                .get()
//                .getName());
//    }
//
//    @Test
//    void updateFailNotFound() {
//        GiftCertificate giftCertificateDao = GiftCertificate.builder()
//                .id(5L)
//                .name("Updated name for test")
//                .build();
//
//        assertThrows(EntityNotFoundException.class, () -> jdbcTemplateGiftDao.update(giftCertificateDao));
//    }
//
//    @Test
//    void deleteSuccess() {
//        Long id = 1L;
//        int sizeBefore = jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter()).size();
//
//        boolean res = jdbcTemplateGiftDao.delete(id);
//
//        assertTrue(res);
//        assertEquals(sizeBefore-1, jdbcTemplateGiftDao.findAllEntities(new GiftSearchFilter()).size());
//    }
//
//    @Test
//    void deleteFailNotFound() {
//        Long id = 15175515L;
//
//        assertFalse(jdbcTemplateGiftDao.delete(id));
//    }
//}