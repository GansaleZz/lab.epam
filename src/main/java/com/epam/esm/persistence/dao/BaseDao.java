package com.epam.esm.persistence.dao;


import java.util.Optional;

public interface BaseDao<K,T> {

    /**
     * Searching entity on db by id.
     * @param id unique parameter of every entity, by which we can find it on db.
     * @return entity if it exists and empty optional if not.
     */
    Optional<T> findEntityById(K id);

    /**
     * Creating entity on db.
     * @param t entity which we want to create on db.
     * @return entity with id which was created.
     */
    T createEntity(T t);

    /**
     * Deleting entity from db.
     * @param id of entity which we want to delete from db.
     * @return returns true if entity was deleted, else returns false.
     */
    boolean deleteEntity(K id);
}
