package com.epam.esm.persistence.jpa.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaTagDao implements TagDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findAllEntities() {
        return null;
    }

    @Override
    public Optional<Tag> findEntityById(Long id) {
        return Optional.empty();
    }

    /*todo if input tag already exists, then return it with id (find entity by name),
        else - creating tag and then return it with id.
    */
    @Override
    public Tag create(Tag tag) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    private Tag findTagByName(Tag tag) {
        return null;
    }
}
