package com.epam.esm.pagination;

import com.epam.esm.TestConfigJpa;
import com.epam.esm.web.controller.TagController;
import com.epam.esm.web.util.pagination.PageFilter;
import com.epam.esm.web.util.pagination.PaginationEntityLinkHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfigJpa.class)
public class PaginationEntityLinkTest {

    @Autowired
    private PaginationEntityLinkHelper paginationEntityLink;

    @Test
    void nextLinkExists() {
        String expectedQuery = "page=1&items=1";
        PageFilter pageFilter = PageFilter.builder()
                .page(0)
                .items(1)
                .count(2)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveNextPageLink(pageFilter, link);

        assertTrue(newLink.isPresent());
        assertEquals(expectedQuery, newLink.get().toUri().getQuery());
    }

    @Test
    void nextLinkDoesNotExist() {
        PageFilter pageFilter = PageFilter.builder()
                .page(1123)
                .items(1)
                .count(1124)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveNextPageLink(pageFilter, link);

        assertFalse(newLink.isPresent());
    }

    @Test
    void previousLinkExists() {
        String expectedQuery = "page=0&items=1";
        PageFilter pageFilter = PageFilter.builder()
                .page(1)
                .items(1)
                .count(2)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrievePreviousPageLink(pageFilter, link);

        assertTrue(newLink.isPresent());
        assertEquals(expectedQuery, newLink.get().toUri().getQuery());
    }

    @Test
    void previousLinkDoesNotExist() {
        PageFilter pageFilter = PageFilter.builder()
                .page(0)
                .items(1)
                .count(124)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrievePreviousPageLink(pageFilter, link);

        assertFalse(newLink.isPresent());
    }

    @Test
    void firstLinkExists() {
        String expectedQuery = "page=0&items=1";
        PageFilter pageFilter = PageFilter.builder()
                .page(140)
                .items(1)
                .count(223)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveFirstPageLink(pageFilter, link);

        assertTrue(newLink.isPresent());
        assertEquals(expectedQuery, newLink.get().toUri().getQuery());
    }

    @Test
    void firstLinkDoesNotExist() {
        PageFilter pageFilter = PageFilter.builder()
                .page(0)
                .items(1)
                .count(223)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveFirstPageLink(pageFilter, link);

        assertFalse(newLink.isPresent());
    }

    @Test
    void lastLinkExists() {
        String expectedQuery = "page=222&items=1";
        PageFilter pageFilter = PageFilter.builder()
                .page(140)
                .items(1)
                .count(223)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveLastPageLink(pageFilter, link);

        assertTrue(newLink.isPresent());
        assertEquals(expectedQuery, newLink.get().toUri().getQuery());
    }

    @Test
    void lastLinkDoesNotExist() {
        String expectedQuery = "page=222&items=1";
        PageFilter pageFilter = PageFilter.builder()
                .page(222)
                .items(1)
                .count(223)
                .build();
        Link link = allTagsLink(pageFilter).withSelfRel();

        Optional<Link> newLink = paginationEntityLink.retrieveLastPageLink(pageFilter, link);

        assertFalse(newLink.isPresent());
    }

    private Link allTagsLink(PageFilter pageFilter) {
        return Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .retrieveAllTags(pageFilter, null))
                .toString());
    }
}
