package com.epam.esm.pagination;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.web.controller.GiftCertificateController;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationGiftCertificateLink;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigJpa.class)
public class PaginationGiftCertificateLinkTest {

    @Autowired
    private PaginationGiftCertificateLink paginationGiftCertificateLink;
    private final Method listOfGiftCertificates;

    public PaginationGiftCertificateLinkTest() throws NoSuchMethodException {
        listOfGiftCertificates = GiftCertificateController.class
                .getMethod("listOfGiftCertificates",
                        PaginationFilter.class,
                        GiftCertificateSearchFilter.class,
                        BindingResult.class);
    }

    @Test
    void nextLinkExists() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(0)
                .items(1)
                .count(2)
                .build();
        String expectedQuery = "page=1&items=1";


        Optional<Link> link = paginationGiftCertificateLink.nextLink(listOfGiftCertificates,
                paginationFilter,
                null,
                null);

        assertTrue(link.isPresent());
        assertEquals(expectedQuery, link.get().toUri().getQuery());
    }

    @Test
    void nextLinkDoesNotExist() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(1123)
                .items(1)
                .count(1124)
                .build();

        Optional<Link> link = paginationGiftCertificateLink
                .nextLink(listOfGiftCertificates, paginationFilter, null, null);

        assertFalse(link.isPresent());
    }

    @Test
    void previousLinkExists() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(1)
                .items(1)
                .count(2)
                .build();
        String expectedQuery = "page=0&items=1";

        Optional<Link> link = paginationGiftCertificateLink
                .prevLink(listOfGiftCertificates, paginationFilter, null, null);

        assertTrue(link.isPresent());
        assertEquals(expectedQuery, link.get().toUri().getQuery());
    }

    @Test
    void previousLinkDoesNotExist() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(0)
                .items(1)
                .count(124)
                .build();

        Optional<Link> link = paginationGiftCertificateLink
                .prevLink(listOfGiftCertificates, paginationFilter, null, null);

        assertFalse(link.isPresent());
    }

    @Test
    void firstLinkExists() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(140)
                .items(1)
                .count(223)
                .build();
        String expectedQuery = "page=0&items=1";

        Optional<Link> link = paginationGiftCertificateLink
                .firstLink(listOfGiftCertificates, paginationFilter, null,null);

        assertTrue(link.isPresent());
        assertEquals(expectedQuery, link.get().toUri().getQuery());
    }

    @Test
    void firstLinkDoesNotExist() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(0)
                .items(1)
                .count(223)
                .build();

        Optional<Link> link = paginationGiftCertificateLink
                .firstLink(listOfGiftCertificates, paginationFilter, null,null);

        assertFalse(link.isPresent());
    }

    @Test
    void lastLinkExists() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(140)
                .items(1)
                .count(223)
                .build();
        String expectedQuery = "page=222&items=1";

        Optional<Link> link = paginationGiftCertificateLink
                .lastLink(listOfGiftCertificates, paginationFilter, null, null);

        assertTrue(link.isPresent());
        assertEquals(expectedQuery, link.get().toUri().getQuery());
    }

    @Test
    void lastLinkDoesNotExist() {
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(222)
                .items(1)
                .count(223)
                .build();

        Optional<Link> link = paginationGiftCertificateLink
                .lastLink(listOfGiftCertificates, paginationFilter, null, null);

        assertFalse(link.isPresent());
    }
}
