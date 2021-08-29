package com.epam.esm.persistence.jdbc.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.jdbc.JdbcTemplateBaseDao;

import java.util.List;
import java.util.Optional;

public interface JdbcTemplateTagDao extends JdbcTemplateBaseDao<Long, TagDao> {

    /**
     * Method created for searching all the tags on db.
     * @return list of found gift certificates.
     */
    List<TagDao> findAllEntities();

    /**
     * Method created for search tag on db by its name.
     * @param name unique parameter of every tag, by which we can find it on db.
     * @return tag if it exists and empty optional if not.
     */
    Optional<TagDao> findTagByName(String name);
}
