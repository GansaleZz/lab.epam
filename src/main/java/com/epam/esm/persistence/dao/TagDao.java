package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.util.pagination.PageFilter;

import java.util.List;
import java.util.Optional;

public interface TagDao extends BaseDao<Long, Tag> {

    /**
     * Searching all the tags on db.
     * @param paginationFilter object which contains information about page's number
     *                         and number of items for paging.
     * @return list of found tags.
     */
    List<Tag> findAllTags(PageFilter paginationFilter);

    /**
     * Searching tag on db by name.
     * @param tag instance which contains tag name.
     * @return tag if exists else return empty optional.
     */
    Optional<Tag> findTagByName(Tag tag);

    /**
     * Searching for the tag that most often occurs in orders of
     * user with the highest cost of all orders.
     * @param id user's id.
     * @return found tag.
     */
    Tag findMostWidelyUsedTag(Long id);
}
