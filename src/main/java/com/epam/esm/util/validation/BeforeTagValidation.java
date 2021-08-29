package com.epam.esm.util.validation;


import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;

import java.util.Optional;

public interface BeforeTagValidation<T, K> {

    /**
     * Method which created for check existence of tag before using method of deleting
     * it from bd.
     * @param isPresent boolean that indicates existence of tag.
     * @param id of tag.
     * @throws EntityNotFoundException if tag does not exist.
     */
    void onBeforeDelete(Boolean isPresent, K id) throws EntityNotFoundException;

    /**
     * Method which created for check existence of tag before using method of finding
     * it by id on bd.
     * @param t wrapped by optional - if does not exist then optional of null.
     * @param id of tag.
     * @throws EntityNotFoundException if tag does not exist.
     */
    void onBeforeFindEntity(Optional<T> t, K id) throws EntityNotFoundException;

    /**
     * Method which created for check existence of tag before using method of finding
     * it by name on bd.
     * @param t wrapped by optional - if does not exist then optional of null.
     * @param name of tag.
     * @throws EntityNotFoundException if tag does not exist.
     */
    void onBeforeFindEntity(Optional<T> t, String name) throws EntityNotFoundException;

    /**
     * Method which created for check params for creating tag before using
     * method of creating it on bd.
     * @param t object of tag class with params for creating.
     * @throws EntityBadInputException if one of non null params is null.
     */
    void onBeforeInsert(T t) throws EntityBadInputException;
}
