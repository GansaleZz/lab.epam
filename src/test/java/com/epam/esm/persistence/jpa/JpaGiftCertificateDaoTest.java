package com.epam.esm.persistence.jpa;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.jpa.giftCertificate.JpaGiftCertificateDao;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { TestConfigJpa.class }, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = "classpath:sql/db-init.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:sql/db-clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
public class JpaGiftCertificateDaoTest {

    @Autowired
    private JpaGiftCertificateDao jpaGiftCertificateDao;

    @Test
    public void findAllGiftsCertificatesWithoutSearchFilterParams() {
        int sizeAfterInit = 4;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter);

        assertEquals(sizeAfterInit, listResult.size());
    }

    @Test
    void findAllGiftsCertificatesWithTagNotExists() {
        String tagName = "Hey hey hey, Test!";
        int paginationItems = 1000;
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter
                .builder()
                .tags(Collections.singletonList(tagName))
                .build();
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftsCertificatesWithTagExists() {
        String tagName = "TAG_TEST_1";
        int paginationItems = 1000;
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter
                .builder()
                .tags(Collections.singletonList(tagName))
                .build();
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(1, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNamePartExists() {
        String namePart = "TEST";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftName(namePart)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listFirstResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(4, listFirstResult.size());
        listFirstResult.forEach(giftCertificateDao ->  assertTrue(giftCertificateDao.getName().contains(namePart)));
    }

    @Test
    void findAllGiftCertificatesByNamePartNotExists() {
        String namePart = "test";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftName(namePart)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByDescriptionPartExists() {
        String descriptionPart = "_3";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftDescription(descriptionPart)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(1, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNameDescriptionNotExists() {
        String descriptionPart = "_3!!";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftDescription(descriptionPart)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(0, listResult.size());
    }

    @Test
    void findAllGiftCertificatesByNameAscOrder() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftsByNameOrder(QueryOrder.ASC)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals("TEST1",listResult.get(0).getName());
    }

    @Test
    void findAllGiftCertificatesByDateDescOrder() {
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftsByNameOrder(QueryOrder.DESC)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals("TEST1",listResult.get(listResult.size()-1 ).getName());
    }

    @Test
    void findAllGiftCertificatesWithAllParamsExists() {
        String tagName = "TAG_TEST_1";
        String namePart = "TEST";
        String descriptionPart = "_";
        GiftCertificateSearchFilter giftSearchFilter = GiftCertificateSearchFilter.builder()
                .giftName(namePart)
                .tags(Collections.singletonList(tagName))
                .giftDescription(descriptionPart)
                .giftsByNameOrder(QueryOrder.ASC)
                .giftsByDateOrder(QueryOrder.DESC)
                .build();
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();

        List<GiftCertificate> listResult = jpaGiftCertificateDao
                .findAllGiftCertificates(giftSearchFilter, paginationFilter);

        assertEquals(1, listResult.size());
        listResult.forEach(giftCertificateDao ->
                assertEquals(tagName, giftCertificateDao.getTags().get(0).getName())
        );
    }

    @Test
    void findGiftCertificateByIdExists() {
        Long id = 1L;

        Optional<GiftCertificate> giftCertificateDao = jpaGiftCertificateDao.findEntityById(id);

        assertTrue(giftCertificateDao.isPresent());
        assertEquals(id, giftCertificateDao.get().getGiftId());
    }

    @Test
    void findGiftCertificateByIdFailNotFound() {
        Long badId = 150L;

        assertEquals(Optional.empty(), jpaGiftCertificateDao.findEntityById(badId));
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

        GiftCertificate giftCertificate = jpaGiftCertificateDao.create(giftCertificateDao);
        giftCertificateDao.setCreateDate(giftCertificate.getCreateDate().toLocalDate().atStartOfDay());
        giftCertificateDao.setLastUpdateDate(giftCertificate.getLastUpdateDate().toLocalDate().atStartOfDay());

        assertEquals(giftCertificateDao, jpaGiftCertificateDao.findEntityById(giftCertificate.getGiftId()).get());
    }

    @Test
    void createFailBadInput() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .name("Hey Hey")
                .build();

        assertThrows(EntityBadInputException.class, () -> jpaGiftCertificateDao.create(giftCertificateDao));
    }

    @Test
    void updateSuccess() {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .giftId(1L)
                .name("Updated name for test")
                .build();

        jpaGiftCertificateDao.update(giftCertificateDao);

        assertEquals(giftCertificateDao.getName(), jpaGiftCertificateDao
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

        assertThrows(EntityNotFoundException.class, () -> jpaGiftCertificateDao.update(giftCertificateDao));
    }

    @Test
    void deleteSuccess() {
        Long id = 1L;
        int paginationItems = 1000;
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .items(paginationItems)
                .build();
        int sizeBefore = jpaGiftCertificateDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter).size();

        boolean res = jpaGiftCertificateDao.delete(id);

        assertTrue(res);
        assertEquals(sizeBefore-1, jpaGiftCertificateDao
                .findAllGiftCertificates(new GiftCertificateSearchFilter(), paginationFilter).size());
    }

    @Test
    void deleteFailNotFound() {
        Long id = 15175515L;
        int size = jpaGiftCertificateDao.findAllGiftCertificates(new GiftCertificateSearchFilter(),
                PaginationFilter.builder()
                        .items(1000)
                        .build()).size();

        jpaGiftCertificateDao.delete(id);
        int sizeAfterDelete = jpaGiftCertificateDao.findAllGiftCertificates(new GiftCertificateSearchFilter(),
                PaginationFilter.builder()
                        .items(1000)
                        .build()).size();

        assertEquals(size, sizeAfterDelete);
    }
}
