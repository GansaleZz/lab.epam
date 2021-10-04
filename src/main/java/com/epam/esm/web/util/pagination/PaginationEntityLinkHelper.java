package com.epam.esm.web.util.pagination;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Component
public class PaginationEntityLinkHelper {

    private static final String PAGE = "page";
    private static final String ITEMS = "items";
    private static final String NEXT = "next";
    private static final String PREVIOUS = "previous";
    private static final String FIRST = "first";
    private static final String LAST = "last";

    /**
     * Sets pagination parameters as map values.
     * @param pageFilter object which contains information about pagination.
     * @return resulting map.
     */
    public static MultiValueMap<String, String> getPagingParameters(PageFilter pageFilter) {
        MultiValueMap<String, String> pagingParameters = new LinkedMultiValueMap<>();

        pagingParameters.add(PaginationEntityLinkHelper.PAGE, String.valueOf(pageFilter.getPage()));
        pagingParameters.add(PaginationEntityLinkHelper.ITEMS, String.valueOf(pageFilter.getItems()));

        return pagingParameters;
    }

    /**
     * Creates link on next page.
     * @param pageFilter object which contains information about pagination.
     * @param link to getting list of entities.
     * @return link on next page if current page is not last.
     */
    public Optional<Link> retrieveNextPageLink(PageFilter pageFilter,
                                               Link link) {
        return createNewLink(link,
                provideNextPage(pageFilter),
                NEXT);
    }

    /**
     * Creates link on previous page.
     * @param pageFilter object which contains information about pagination.
     * @param link to getting list of entities.
     * @return link on previous page if current page is not first.
     */
    public Optional<Link> retrievePreviousPageLink(PageFilter pageFilter,
                                                   Link link) {
        return createNewLink(link,
                providePreviousPage(pageFilter),
                PREVIOUS);
    }

    /**
     * Creates link on first page.
     * @param pageFilter object which contains information about pagination.
     * @param link to getting list of entities.
     * @return link on first page if current page is not first.
     */
    public Optional<Link> retrieveFirstPageLink(PageFilter pageFilter,
                                                Link link) {
        return createNewLink(link,
                provideFirstPage(pageFilter),
                FIRST);
    }

    /**
     * Creates link on last page.
     * @param pageFilter object which contains information about pagination.
     * @param link to getting list of entities.
     * @return link on last page if current page is not last.
     */
    public Optional<Link> retrieveLastPageLink(PageFilter pageFilter,
                                               Link link) {
        return createNewLink(link,
                provideLastPage(pageFilter),
                LAST);
    }

    private Optional<PageFilter> provideNextPage(PageFilter pageFilter) {
        int countOfPages = calculateCountOfPages(pageFilter);

        if (pageFilter.getPage() < countOfPages) {
            return Optional.of(PageFilter.builder()
                    .page(pageFilter.getPage() + 1)
                    .items(pageFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<PageFilter> providePreviousPage(PageFilter pageFilter) {
        int maxPages = calculateCountOfPages(pageFilter);

        if (pageFilter.getPage() > 0 &&
                pageFilter.getPage() <= maxPages) {
            return Optional.of(PageFilter.builder()
                    .page(pageFilter.getPage()-1)
                    .items(pageFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<PageFilter> provideFirstPage(PageFilter pageFilter) {
        if (pageFilter.getPage() !=0) {
            return Optional.of(PageFilter.builder()
                    .page(0)
                    .items(pageFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private Optional<PageFilter> provideLastPage(PageFilter pageFilter) {
        int countOfPages = calculateCountOfPages(pageFilter);

        if (pageFilter.getPage() < countOfPages) {
            return Optional.of(PageFilter.builder()
                    .page(countOfPages)
                    .items(pageFilter.getItems())
                    .build());
        } else {
            return Optional.empty();
        }
    }

    private int calculateCountOfPages(PageFilter pageFilter) {
        int countOfPages = -1;
        long startCount = pageFilter.getCount();
        do {
            startCount -= pageFilter.getItems();
            countOfPages++;
        } while (startCount > 0);
        return countOfPages;
    }

    private Optional<Link> createNewLink(Link link,
                                         Optional<PageFilter> newPageFilter,
                                         String rel) {
        Optional<Link> newPageLink = Optional.empty();

        if (newPageFilter.isPresent()) {
            newPageLink = Optional.of(Link.of(UriComponentsBuilder.fromUri(link.toUri())
                    .replaceQueryParam(PAGE, newPageFilter.get().getPage())
                    .replaceQueryParam(ITEMS, newPageFilter.get().getItems())
                    .build(true)
                    .toString())
                    .withRel(rel));
        }

        return newPageLink;
    }
}
