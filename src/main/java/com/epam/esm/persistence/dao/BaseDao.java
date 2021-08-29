package com.epam.esm.persistence.dao;

import com.epam.esm.model.dto.AbstractEntityDto;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public interface BaseDao<K,T extends AbstractEntityDto> {

    void setDataSource(DataSource dataSource);

    List<T> findAllEntities();

    Optional<T> findEntityById(K id);

    boolean create(T t);

    boolean delete(K id);
}
