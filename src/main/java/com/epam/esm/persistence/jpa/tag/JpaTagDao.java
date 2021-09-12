package com.epam.esm.persistence.jpa.tag;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaTagDao implements TagDao {

    private static final String NAME = "name";
    private static final String TAG_ID = "tagId";
    private static final String USER_ID = "userId";
    private static final String EMPTY_STRING = "";
    private static final String GIFT_CERTIFICATE = "giftCertificate";
    private static final String TAGS = "tags";
    private static final String ORDERS = "orders";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findAllEntities() {
        return entityManager.createQuery(createQueryByParam(EMPTY_STRING, new Object()))
                .getResultList();
    }

    @Override
    public Tag findMostWidelyUsedTag(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        Root<User> root = criteriaQuery.from(User.class);
        Join<Object, Object> join = root.join(ORDERS, JoinType.INNER)
                .on(criteriaBuilder.equal(root.get(USER_ID), id));
        Join<GiftCertificate, Order> joinGift = join
                .join(GIFT_CERTIFICATE, JoinType.LEFT);
        Join<GiftCertificate, Tag> joinTag = joinGift
                .join(TAGS, JoinType.LEFT);

        criteriaQuery.multiselect(joinTag.get(TAG_ID),
                criteriaBuilder.count(joinTag.get(NAME)))
                .groupBy(joinTag.get(TAG_ID))
                .orderBy(criteriaBuilder.desc(criteriaBuilder.count(joinTag.get(NAME))));

        return findEntityById((Long) Arrays.stream(entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findAny()
                .get())
                .findAny()
                .get())
                .get();
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
