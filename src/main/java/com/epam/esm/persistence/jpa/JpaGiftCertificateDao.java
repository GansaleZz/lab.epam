package com.epam.esm.persistence.jpa;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaGiftCertificateDao implements GiftCertificateDao {

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PERCENT = "%";
    private static final String TAGS = "tags";
    private static final String CREATE_DATE = "createDate";
    private static final String TAG_DOES_NOT_EXIST = "Tag does not exist";

    @PersistenceContext
    private EntityManager entityManager;
    private final TagDao tagDao;
    private final Clock clock;

    @Autowired
    public JpaGiftCertificateDao(TagDao tagDao, Clock clock) {
        this.tagDao = tagDao;
        this.clock = clock;
    }

    @Override
    public List<GiftCertificate> findAllGiftCertificates(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                                        PageFilter pageFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder
                .createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root).distinct(true);

        criteriaQuery.where(criteriaBuilder.and(makePredicateList(giftCertificateSearchFilter, root)
                .toArray(new Predicate[] {})));
        criteriaQuery.orderBy(orderClause(giftCertificateSearchFilter, criteriaBuilder, root));

        pageFilter.setCount(countResult(giftCertificateSearchFilter));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(pageFilter.getPage() * pageFilter.getItems())
                .setMaxResults(pageFilter.getItems())
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findEntityById(Long id) {
        return Optional.ofNullable(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    @Transactional
    public GiftCertificate createEntity(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now(clock));
        giftCertificate.setLastUpdateDate(LocalDateTime.now(clock));
        giftCertificate.getTags().forEach(tagDao::createEntity);

        entityManager.persist(giftCertificate);

        return giftCertificate;
    }

    @Override
    public GiftCertificate updateGiftCertificate(GiftCertificate giftCertificate) {
        GiftCertificate updatableGiftCertificate = findEntityById(giftCertificate.getGiftId())
                .orElseThrow(() -> new  EntityNotFoundException(giftCertificate.getGiftId().toString()));

        if (giftCertificate.getName() != null) {
            updatableGiftCertificate.setName(giftCertificate.getName());
        }
        if (giftCertificate.getDescription() != null) {
            updatableGiftCertificate.setDescription(updatableGiftCertificate.getDescription());
        }
        if (giftCertificate.getDuration() != null) {
            updatableGiftCertificate.setDuration(giftCertificate.getDuration());
        }
        if (giftCertificate.getPrice() != null) {
            updatableGiftCertificate.setPrice(giftCertificate.getPrice());
        }
        if (giftCertificate.getTags().size() != 0) {
            giftCertificate.getTags()
                    .forEach(tagDao::createEntity);
            updatableGiftCertificate.getTags().clear();
            updatableGiftCertificate.getTags().addAll(giftCertificate.getTags());
        }
        updatableGiftCertificate.setLastUpdateDate(LocalDateTime.now(clock));

        return updatableGiftCertificate;
    }

    @Override
    @Transactional
    public boolean deleteEntity(Long id) {
        findEntityById(id).ifPresent(certificate -> entityManager.remove(certificate));
        return true;
    }

    private List<Order> orderClause(GiftCertificateSearchFilter giftSearchFilter,
                                    CriteriaBuilder criteriaBuilder,
                                    Root<GiftCertificate> root) {
        List<Order> orderList = new ArrayList<>();

        if (giftSearchFilter.getGiftCertificatesByNameOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftCertificatesByNameOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get(NAME)));
            } else {
                orderList.add(criteriaBuilder.desc(root.get(NAME)));
            }
        }

        if (giftSearchFilter.getGiftCertificatesByDateOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftCertificatesByDateOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get(CREATE_DATE)));
            } else {
                orderList.add(criteriaBuilder.desc(root.get(CREATE_DATE)));
            }
        }

        return orderList;
    }

    private long countResult(GiftCertificateSearchFilter giftCertificateSearchFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.select(criteriaBuilder.count(root));

        cq.where(criteriaBuilder.and(makePredicateList(giftCertificateSearchFilter, root)
                .toArray(new Predicate[] {})));

        return entityManager.createQuery(cq).getSingleResult();
    }

    private List<Predicate> makePredicateList(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                              Root<?> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        List<Predicate> predicates = new ArrayList<>();

        if (giftCertificateSearchFilter.getTags().size() != 0) {
            List<Tag> tagList = new ArrayList<>();
            giftCertificateSearchFilter.getTags()
                    .forEach(tagName -> tagDao.findTagByName(Tag.builder().name(tagName).build())
                            .map(tagList::add));

            if (giftCertificateSearchFilter.getTags().size() != tagList.size()) {
                // Using this tag, when user set non-existent tag as param
                tagList.add(Tag.builder()
                        .tagId(0L)
                        .name(TAG_DOES_NOT_EXIST)
                        .build());
            }

            List<Predicate> predicatesForTags = new ArrayList<>();
            tagList.forEach(tag -> predicatesForTags.add(criteriaBuilder
                    .isMember(tag, root.get(TAGS))));

            predicates.add(criteriaBuilder.and(predicatesForTags.toArray(new Predicate[]{})));
        }

        if (giftCertificateSearchFilter.getGiftCertificateName() != null) {
            predicates.add(predicateByPartOfProperty(root,
                    giftCertificateSearchFilter.getGiftCertificateName(),
                    NAME));
        }

        if (giftCertificateSearchFilter.getGiftCertificateDescription() != null) {
            predicates.add(predicateByPartOfProperty(root,
                    giftCertificateSearchFilter.getGiftCertificateDescription(),
                    DESCRIPTION));
        }

        return predicates;
    }

    private Predicate predicateByPartOfProperty(Root<?> root,
                                                String propertyPart,
                                                String propertyName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        return criteriaBuilder.like(root.get(propertyName),
                PERCENT + propertyPart + PERCENT);
    }
}