package com.epam.esm.pagination;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationOrderLink;
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
public class PaginationOrderLinkTest {

    @Autowired
    private PaginationOrderLink paginationOrderLink;
    private final Method listOfOrders;

    public PaginationOrderLinkTest() throws NoSuchMethodException {
        listOfOrders = OrderController.class
                .getMethod("listOfUserOrders",
                        PaginationFilter.class,
                        Long.class,
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


        Optional<Link> link = paginationOrderLink.nextLink(listOfOrders,
                0L,
                paginationFilter,
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

        Optional<Link> link = paginationOrderLink
                .nextLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .prevLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .prevLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .firstLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .firstLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .lastLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

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

        Optional<Link> link = paginationOrderLink
                .lastLink(listOfOrders,
                        0L,
                        paginationFilter,
                        null);

        assertFalse(link.isPresent());
    }
}
