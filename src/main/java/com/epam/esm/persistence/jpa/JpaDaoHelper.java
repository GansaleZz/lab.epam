package com.epam.esm.persistence.jpa;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
class JpaDaoHelper<T> {
    private static final String EMPTY_STRING = "";

    @PersistenceContext
    private EntityManager entityManager;

    CriteriaQuery<T> createQueryByParam(String attributeName,
                                        Object attributeValue,
                                        Class<T> clazz) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = criteriaBuilder.createQuery(clazz);
        Root<T> root = criteriaQuery.from(clazz);
        criteriaQuery.select(root);

        if (!attributeName.equals(EMPTY_STRING)) {
            criteriaQuery.where(criteriaBuilder.equal(root.get(attributeName),
                    attributeValue));
        }

        return criteriaQuery;
    }
}
