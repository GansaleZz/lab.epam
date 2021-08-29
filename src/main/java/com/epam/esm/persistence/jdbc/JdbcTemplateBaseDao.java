package com.epam.esm.persistence.jdbc;


import java.util.Optional;

public interface JdbcTemplateBaseDao<K,T> {

    /**
     * Method created for search entity on db by id.
     * @param id unique parameter of every entity, by which we can find it on db.
     * @return entity if it exists and empty optional if not.
     */
    Optional<T> findEntityById(K id);

    /**
     * Method created for create entity on db.
     * @param t - entity which we want create on db.
     * @return entity which was created.
     */
    T create(T t);

    /**
     * Method created for delete entity from db.
     * @param id of entity which we want to delete from db.
     * @return indicator of deletes result from db.
     */
    boolean delete(K id);
}
