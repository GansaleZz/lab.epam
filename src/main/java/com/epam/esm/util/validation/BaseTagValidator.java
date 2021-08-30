package com.epam.esm.util.validation;


import com.epam.esm.web.exception.EntityBadInputException;

public interface BaseTagValidator<T, K> {
    /**
     * Checks params for creating tag before using
     * method of creating it on bd.
     * @param t object of tag class with params for creating.
     * @throws EntityBadInputException if one of non null params is null.
     */
    void onBeforeInsert(T t) throws EntityBadInputException;
}
