package com.epam.esm.web.controller;

import com.epam.esm.web.util.pagination.PageFilter;
import com.epam.esm.web.util.pagination.PaginationEntityLinkHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

@Component
class ControllerHelper<T> {

    private final PaginationEntityLinkHelper paginationEntityLinkHelper;

    @Autowired
    ControllerHelper(PaginationEntityLinkHelper paginationEntityLinkHelper) {
        this.paginationEntityLinkHelper = paginationEntityLinkHelper;
    }

    public void retrievePaginationLinks(PageFilter pageFilter,
                                        Link link,
                                        CollectionModel<EntityModel<T>> result) {
        if (pageFilter.getItems() != 0) {
            paginationEntityLinkHelper.retrieveNextPageLink(pageFilter, link)
                    .ifPresent(result::add);
            paginationEntityLinkHelper.retrievePreviousPageLink(pageFilter, link)
                    .ifPresent(result::add);
            paginationEntityLinkHelper.retrieveFirstPageLink(pageFilter, link)
                    .ifPresent(result::add);
            paginationEntityLinkHelper.retrieveLastPageLink(pageFilter, link)
                    .ifPresent(result::add);
        }
    }
}
