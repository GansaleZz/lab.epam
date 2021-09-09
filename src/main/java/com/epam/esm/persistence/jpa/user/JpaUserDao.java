package com.epam.esm.persistence.jpa.user;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserDao implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAllEntities() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);

        return entityManager.createQuery(criteriaQuery.select(root)).getResultList();
    }

    @Override
    public Optional<User> findEntityById(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.and(criteriaBuilder
                .equal(root.get("userId"), id));
        criteriaQuery.where(predicate);

        return entityManager.createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .findAny();
    }
}
