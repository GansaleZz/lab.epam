package com.epam.esm.web.util.pagination.link;

import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.lang.reflect.Method;
import java.util.Optional;

@Component
public class PaginationOrderLink extends PaginationEntityLink {

    public Optional<Link> nextLink(Method method,
                                   Long userId,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = nextCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        userId,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(NEXT));
    }

    public Optional<Link> prevLink(Method method,
                                   Long userId,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = prevCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        userId,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(PREV));
    }

    public Optional<Link> firstLink(Method method,
                                    Long userId,
                                    PaginationFilter paginationFilter,
                                    BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = firstCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        userId,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(FIRST));
    }

    public Optional<Link> lastLink(Method method,
                                   Long userId,
                                   PaginationFilter paginationFilter,
                                   BindingResult bindingResult) {
        Optional<PaginationFilter> newPaginationFilter = lastCheck(paginationFilter);

        return newPaginationFilter.map(filter -> WebMvcLinkBuilder.linkTo(method,
                        userId,
                        paginationFilter,
                        bindingResult)
                .slash(String.format(PAGE_ITEMS, filter.getPage(), filter.getItems()))
                .withRel(LAST));
    }
}
