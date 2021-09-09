package com.epam.esm.persistence.jpa.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaTagDao implements TagDao {
    @Override
    public Optional<Tag> findEntityById(Long id) {
        return Optional.empty();
    }

    @Override
    public Tag create(Tag tag) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public List<Tag> findAllEntities() {
        return null;
    }
}
