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

    /**
     * Creates link on next page.
     * @param method which uses for get all entities.
     * @param userId - user's id.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult - need for catch problems with validating.
     * @return link on next page if current page is not last.
     */
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

    /**
     * Creates link on previous page.
     * @param method which uses for get all entities.
     * @param userId - user's id.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult - need for catch problems with validating.
     * @return link on previous page if current page is not first.
     */
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

    /**
     * Creates link on first page.
     * @param method which uses for get all entities.
     * @param userId - user's id.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult - need for catch problems with validating.
     * @return link on first page if current page is not first.
     */
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

    /**
     * Creates link on last page.
     * @param method which uses for get all entities.
     * @param userId - user's id.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult - need for catch problems with validating.
     * @return link on last page if current page is not last.
     */
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
