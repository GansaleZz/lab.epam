package com.epam.esm.persistence.jdbc.tag;

import com.epam.esm.persistence.dao.Tag;
import com.epam.esm.persistence.jdbc.BaseDao;

import java.util.List;
import java.util.Optional;

public interface TagDao extends BaseDao<Long, Tag> {

    /**
     * Searching all the tags on db.
     * @return list of found gift certificates.
     */
    List<Tag> findAllEntities();

    /**
     * Searching tag on db by its name.
     * @param name unique parameter of every tag, by which we can find it on db.
     * @return tag if it exists and empty optional if not.
     */
    Optional<Tag> findTagByName(String name);
}
