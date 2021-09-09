package com.epam.esm.persistence.jpa.gift;

import com.epam.esm.persistence.dao.GiftDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.web.exception.EntityNotFoundException;
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
public class JpaGiftDao implements GiftDao {
   @PersistenceContext
   private EntityManager entityManager;

    @Override
    public List<GiftCertificate> findAllEntities(GiftSearchFilter giftSearchFilter) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder
                .createQuery(GiftCertificate.class);
        Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
        CriteriaQuery<GiftCertificate> all = criteriaQuery.select(root);

        whereClause(giftSearchFilter, all, root, criteriaBuilder);
        all.orderBy(orderClause(giftSearchFilter, criteriaBuilder, root));
        return entityManager.createQuery(all).getResultList();
    }

    @Override
    public Optional<GiftCertificate> findEntityById(Long id) {
        return Optional.of(entityManager.find(GiftCertificate.class, id));
    }

    @Override
    @Transactional
    public GiftCertificate create(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        giftCertificate.getTags().forEach(this::checkExistence);

        entityManager.persist(giftCertificate);

        return giftCertificate;
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        Optional<GiftCertificate> optional = findEntityById(giftCertificate.getId());
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
                        .forEach(this::checkExistence);
                gift.getTags().clear();
                gift.getTags().addAll(giftCertificate.getTags());
            }
            return gift;
        } else {
            throw new EntityNotFoundException("Requested gift not found");
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) {
        Optional<GiftCertificate> giftCertificate = findEntityById(id);
        if (giftCertificate.isPresent()) {
            entityManager.remove(giftCertificate.get());
            return true;
        } else {
            return false;
        }
    }

    private void whereClause(GiftSearchFilter giftSearchFilter,
                             CriteriaQuery<GiftCertificate> all,
                             Root<GiftCertificate> root,
                             CriteriaBuilder criteriaBuilder) {
        Predicate partOfName = criteriaBuilder.like(root.get("name"),
                "%" + giftSearchFilter.getGiftName() + "%");
        Predicate partOfDescription = criteriaBuilder.like(root.get("description"),
                "%" + giftSearchFilter.getGiftDescription() + "%" );
        Join<GiftCertificate, Tag> tags = root.join("tags", JoinType.LEFT);
        Predicate tag = criteriaBuilder.and(
                criteriaBuilder.equal(tags.get("name"), giftSearchFilter.getTag()));

        if (giftSearchFilter.getGiftName() != null) {
            if (giftSearchFilter.getGiftDescription() != null) {
                if (giftSearchFilter.getTag() != null) {
                    all.where(partOfName, partOfDescription, tag);
                } else {
                    all.where(partOfName, partOfDescription);
                }
            } else {
                if (giftSearchFilter.getTag() != null) {
                    all.where(partOfName, tag);
                } else {
                    all.where(partOfName);
                }
            }
        } else {
            if (giftSearchFilter.getGiftDescription() != null) {
                if (giftSearchFilter.getGiftName() != null) {
                    if (giftSearchFilter.getTag() != null) {
                        all.where(partOfName, partOfDescription, tag);
                    } else {
                        all.where(partOfName, partOfDescription);
                    }
                } else {
                    if (giftSearchFilter.getTag() != null) {
                        all.where(tag, partOfDescription);
                    } else {
                        all.where(partOfDescription);
                    }
                }
            } else {
                if (giftSearchFilter.getTag() != null) {
                    if (giftSearchFilter.getGiftName() != null) {
                        if (giftSearchFilter.getGiftDescription() != null) {
                            all.where(partOfName, partOfDescription, tag);
                        } else {
                            all.where(partOfName, tag);
                        }
                    } else {
                        if (giftSearchFilter.getGiftDescription() != null) {
                            all.where(tag, partOfDescription);
                        } else {
                            all.where(tag);
                        }
                    }
                }
            }
        }
    }

    private List<Order> orderClause(GiftSearchFilter giftSearchFilter,
                               CriteriaBuilder criteriaBuilder,
                               Root<GiftCertificate> root) {
        List<Order> orderList = new ArrayList<>();

        if (giftSearchFilter.getGiftsByNameOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftsByNameOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get("name")));
            } else {
                orderList.add(criteriaBuilder.desc(root.get("name")));
            }
        }

        if (giftSearchFilter.getGiftsByDateOrder() != QueryOrder.NO) {
            if (giftSearchFilter.getGiftsByDateOrder() == QueryOrder.ASC) {
                orderList.add(criteriaBuilder.asc(root.get("createDate")));
            } else {
                orderList.add(criteriaBuilder.desc(root.get("createDate")));
            }
        }

        return orderList;
    }

    private void checkExistence(Tag tag) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
        Root<Tag> root = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.like(root.get("name"), tag.getName()));
        List<Tag> list = entityManager.createQuery(criteriaQuery).getResultList();

        if (list.size() != 0) {
            tag.setId(list.get(0).getId());
        } else {
            entityManager.persist(tag);
        }
    }
}