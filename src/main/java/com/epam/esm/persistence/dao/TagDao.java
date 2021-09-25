package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface TagDao extends BaseDao<Long, Tag> {

    /**
     * Searching all the tags on db.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @return list of found tags.
     */
    List<Tag> findAllTags(PaginationFilter paginationFilter);

    /**
     * Searching the most widely used tag of a user with the highest cost of all orders on db.
     * @param id - user's id.
     * @return found tag.
     */
    Tag findMostWidelyUsedTag(Long id);
}
