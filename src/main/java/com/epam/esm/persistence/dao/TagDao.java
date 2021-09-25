package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface TagDao extends BaseDao<Long, Tag> {

    /**
     * Searching all the tags on db.
     * @return list of found gift certificates.
     */
    List<Tag> findAllEntities(PaginationFilter paginationFilter);


    Tag findMostWidelyUsedTag(Long id);
}
