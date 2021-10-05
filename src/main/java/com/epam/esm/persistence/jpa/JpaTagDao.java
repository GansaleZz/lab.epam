package com.epam.esm.persistence.jpa;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Order;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.entity.User;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JpaDaoHelper<Tag> jpaDaoHelper;

    @Autowired
    public JpaTagDao(JpaDaoHelper<Tag> jpaDaoHelper) {
        this.jpaDaoHelper = jpaDaoHelper;
    }

    @Override
    public List<Tag> findAllTags(PageFilter pageFilter) {
        pageFilter.setCount(countResult());

        return entityManager.createQuery(jpaDaoHelper
                        .createQueryByParam(EMPTY_STRING, new Object(), Tag.class))
                .setFirstResult(pageFilter.getPage() * pageFilter.getItems())
                .setMaxResults(pageFilter.getItems())
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
                .getResultList().stream().findAny().get())
                .findAny().get())
                .get();
    }

    @Override
    public Optional<Tag> findEntityById(Long id) {
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    @Override
    @Transactional
    public Tag createEntity(Tag tag) {
        Optional<Tag> optionalTag = findTagByName(tag);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        } else {
            entityManager.persist(tag);
            return tag;
        }
    }

    @Override
    public boolean deleteEntity(Long id) {
        findEntityById(id).ifPresent(tag -> entityManager.remove(tag));
        return true;
    }

    public Optional<Tag> findTagByName(Tag tag) {
        return entityManager.createQuery(jpaDaoHelper
                        .createQueryByParam(NAME, tag.getName(), Tag.class))
                .getResultList()
                .stream()
                .findAny();
    }

    private long countResult() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<Tag> root = cq.from(Tag.class);
        cq.select(criteriaBuilder.count(root));

        return entityManager.createQuery(cq).getSingleResult();
    }
}
