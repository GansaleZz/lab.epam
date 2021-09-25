package com.epam.esm.web.util.pagination.link;

import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class PaginationEntityLink {

    public static final String PAGE_ITEMS = "?page=%s&items=%s";
    protected static final String NEXT = "next";
    protected static final String PREV = "prev";
    protected static final String FIRST = "first";
    protected static final String LAST = "last";

    public Optional<Link> nextLink(Method method,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = nextCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(NEXT));
    }

    public Optional<Link> prevLink(Method method,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = prevCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(PREV));
    }

    public Optional<Link> firstLink(Method method,
                                    PaginationFilter paginationFilter,
                                    BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = firstCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(FIRST));
    }

    public Optional<Link> lastLink(Method method,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = lastCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(LAST));
    }

    protected Optional<PaginationFilter> nextCheck(PaginationFilter paginationFilter) {
        int maxPages = maxPagesCount(paginationFilter);

        if (paginationFilter.getPage() < maxPages) {
            return Optional.of(PaginationFilter.builder()
                    .page(paginationFilter.getPage() + 1)
                    .items(paginationFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    protected Optional<PaginationFilter> prevCheck(PaginationFilter paginationFilter) {
        int maxPages = maxPagesCount(paginationFilter);

        if (paginationFilter.getPage() > 0 &&
            paginationFilter.getPage() <= maxPages) {
            return Optional.of(PaginationFilter.builder()
                    .page(paginationFilter.getPage()-1)
                    .items(paginationFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    protected  Optional<PaginationFilter> firstCheck(PaginationFilter paginationFilter) {
        if (paginationFilter.getPage() !=0) {
            return Optional.of(PaginationFilter.builder()
                    .page(0)
                    .items(paginationFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    protected Optional<PaginationFilter> lastCheck(PaginationFilter paginationFilter) {
        int maxPages = maxPagesCount(paginationFilter);

        if (paginationFilter.getPage() < maxPages) {
            return Optional.of(PaginationFilter.builder()
                    .page(maxPages)
                    .items(paginationFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private int maxPagesCount(PaginationFilter paginationFilter) {
        int maxPages = -1;
        int startCount = paginationFilter.getCount();
        do {
            startCount -= paginationFilter.getItems();
            maxPages++;
        } while (startCount > 0);

        return maxPages;
    }
}
