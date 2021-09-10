package com.epam.esm.persistence.jpa.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.Tag;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaTagDao implements TagDao {

    private static final String NAME = "name";
    private static final String TAG_ID = "tagId";
    private static final String EMPTY_STRING = "";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findAllEntities() {
        return entityManager.createQuery(createQueryByParam(EMPTY_STRING, new Object()))
                .getResultList();
    }

    @Override
    public Optional<Tag> findEntityById(Long id) {
        return entityManager.createQuery(createQueryByParam(TAG_ID, id))
                .getResultList()
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public Tag create(Tag tag) {
        Optional<Tag> optionalTag = findTagByName(tag);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            entityManager.persist(tag);
            return tag;
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<Tag> optionalTag = findEntityById(id);
        optionalTag.ifPresent(tag -> entityManager.remove(tag));
        return true;
    }

    private Optional<Tag> findTagByName(Tag tag) {
        return entityManager.createQuery(createQueryByParam(NAME, tag.getName()))
                .getResultList()
                .stream()
                .findAny();
    }

    private CriteriaQuery<Tag> createQueryByParam(String attributeName,
                                                  Object attributeValue) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.select(root);
        if (!attributeName.equals(EMPTY_STRING)) {
            criteriaQuery.where(criteriaBuilder.equal(root.get(attributeName),
                    attributeValue));
        }
        return criteriaQuery;
    }
}
