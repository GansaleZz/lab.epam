package com.epam.esm.persistence.jpa.user;

import com.epam.esm.persistence.dao.UserDao;
import com.epam.esm.persistence.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserDao implements UserDao {
    @Override
    public List<User> findAllEntities() {
        return null;
    }

    @Override
    public Optional<User> findEntityById(Long id) {
        return Optional.empty();
    }
}
