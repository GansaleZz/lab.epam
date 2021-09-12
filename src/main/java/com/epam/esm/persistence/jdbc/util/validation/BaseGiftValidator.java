package com.epam.esm.persistence.jdbc.util.validation;

import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.web.exception.EntityBadInputException;

public interface BaseGiftValidator<T, K>  {

    /**
     * Checks params for creating gift certificate before using
     * method of creating it on bd.
     * @param t object of gift certificate class with params for creating.
     * @throws EntityBadInputException if one of non null params is null.
     */
    void onBeforeInsert(T t) throws EntityBadInputException;

    /**
     * Checks params of searching filter of gift certificates.
     * @param giftSearchFilter need to set settings of searching gift certificates on bd.
     * @throws EntityBadInputException if one of filters params is incorrect.
     */
    void onBeforeFindAllEntities(GiftSearchFilter giftSearchFilter) throws EntityBadInputException;
}
