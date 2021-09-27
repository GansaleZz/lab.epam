package com.epam.esm.persistence.jpa.giftCertificate;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaGiftCertificateDao implements GiftCertificateDao {

    private static final String ID = "giftId";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PERCENT = "%";
    private static final String TAGS = "tags";
    private static final String CREATE_DATE = "createDate";

    @PersistenceContext
    private EntityManager entityManager;
    private final TagDao tagDao;

    @Autowired
    public JpaGiftCertificateDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Override
    public List<GiftCertificate> findAllGiftCertificates(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                                        PaginationFilter paginationFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder
                .createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();

        if (giftCertificateSearchFilter.getTags().size() != 0) {
            predicates.add(tagsExists(giftCertificateSearchFilter, root));
        }
        if (giftCertificateSearchFilter.getGiftName() != null) {
            predicates.add(partOfNameExists(giftCertificateSearchFilter, root));
        }
        if (giftCertificateSearchFilter.getGiftDescription() != null) {
            predicates.add(partOfDescriptionExists(giftCertificateSearchFilter, root));
        }
        if (!predicates.isEmpty()) {
            criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));
        }
        criteriaQuery.orderBy(orderClause(giftCertificateSearchFilter, criteriaBuilder, root));

        paginationFilter.setCount(countResult(giftCertificateSearchFilter));

        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(paginationFilter.getPage() * paginationFilter.getItems())
                .setMaxResults(paginationFilter.getItems())
                .getResultList();
    }

    @Override
    public Optional<GiftCertificate> findEntityById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder
                .createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        criteriaQuery.select(root);

        criteriaQuery.where(criteriaBuilder.equal(root.get(ID), id));

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findAny();
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.getTags().forEach(tagDao::create);

        entityManager.persist(giftCertificate);

        return giftCertificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        Optional<GiftCertificate> optional = findEntityById(giftCertificate.getGiftId());
        if (optional.isPresent()) {
            GiftCertificate gift = optional.get();
            if (giftCertificate.getName() != null) {
                gift.setName(giftCertificate.getName());
            }
            if (giftCertificate.getDescription() != null) {
                gift.setDescription(gift.getDescription());
            }
            if (giftCertificate.getDuration() != null) {
                gift.setDuration(giftCertificate.getDuration());
            }
            if (giftCertificate.getPrice() != null) {
                gift.setPrice(giftCertificate.getPrice());
            }
            if (giftCertificate.getTags().size() != 0) {
                giftCertificate.getTags()
                        .forEach(tagDao::create);
                gift.getTags().clear();
                gift.getTags().addAll(giftCertificate.getTags());
            }
            return gift;
        } else {
            throw new EntityNotFoundException(giftCertificate.getGiftId().toString());
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<GiftCertificate> giftCertificate = findEntityById(id);
        giftCertificate.ifPresent(certificate -> entityManager.remove(certificate));
        return true;
    }

    private Predicate tagsExists(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                 Root<GiftCertificate> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        Join<GiftCertificate, Tag> join = root.join(TAGS, JoinType.LEFT);
        CriteriaBuilder.In<String> in = criteriaBuilder.in(join.get(NAME));

        giftCertificateSearchFilter.getTags().forEach(in::value);
        return in;
    }

    private Predicate partOfNameExists(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                  Root<GiftCertificate> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        return criteriaBuilder.like(
                root.get(NAME),
                PERCENT + giftCertificateSearchFilter.getGiftName() + PERCENT
        );
    }

    private Predicate partOfDescriptionExists(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                              Root<GiftCertificate> root) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        return criteriaBuilder.like(
                root.get(DESCRIPTION),
                PERCENT + giftCertificateSearchFilter.getGiftDescription() + PERCENT
        );
    }

    private List<Order> orderClause(GiftCertificateSearchFilter giftSearchFilter,
                                    CriteriaBuilder criteriaBuilder,
                                    Root<GiftCertificate> root) {
        List<Order> orderList = new ArrayList<>();

        if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftsByNameOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get(NAME)));
            } else {
                orderList.add(criteriaBuilder.desc(root.get(NAME)));
            }
        }

        if (giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftsByDateOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get(CREATE_DATE)));
            } else {
                orderList.add(criteriaBuilder.desc(root.get(CREATE_DATE)));
            }
        }

        return orderList;
    }

    private int countResult(GiftCertificateSearchFilter giftCertificateSearchFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = criteriaBuilder.createQuery(Long.class);
        Root<GiftCertificate> root = cq.from(GiftCertificate.class);
        cq.select(criteriaBuilder.count(root));
        List<Predicate> predicates = new ArrayList<>();

        if (giftCertificateSearchFilter.getTags().size() != 0) {
            Join<GiftCertificate, Tag> join = root.join(TAGS, JoinType.LEFT);
            CriteriaBuilder.In<String> in = criteriaBuilder.in(join.get(NAME));

            giftCertificateSearchFilter.getTags().forEach(in::value);
            predicates.add(in);
        }
        if (giftCertificateSearchFilter.getGiftName() != null) {
            predicates.add(criteriaBuilder.like(
                    root.get(NAME),
                    PERCENT + giftCertificateSearchFilter.getGiftName() + PERCENT
            ));
        }
        if (giftCertificateSearchFilter.getGiftDescription() != null) {
            predicates.add(criteriaBuilder.like(
                    root.get(DESCRIPTION),
                    PERCENT + giftCertificateSearchFilter.getGiftDescription() + PERCENT
            ));
        }

        cq.where(criteriaBuilder.and(predicates.toArray(new Predicate[] {})));

        return Math.toIntExact(entityManager.createQuery(cq).getSingleResult());
    }
}