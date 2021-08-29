package com.epam.esm.util.validation;

import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;

import java.util.Optional;

public interface BeforeGiftValidation<T, K>  {

    /**
     * Method which created for check existence of gift certificate before using method of deleting
     * it from bd.
     * @param isPresent boolean that indicates existence of gift certificate.
     * @param id of gift certificate.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    void onBeforeDelete(Boolean isPresent, K id) throws EntityNotFoundException;

    /**
     * Method which created for check existence of gift certificate before using method of finding
     * it on bd.
     * @param t wrapped by optional - if not exist then null.
     * @param id of gift certificate.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    void onBeforeFindEntity(Optional<T> t, K id) throws EntityNotFoundException;

    /**
     * Method which created for check params for creating gift certificate before using
     * method of creating it on bd.
     * @param t object of gift certificate class with params for creating.
     * @throws EntityBadInputException if one of non null params is null.
     */
    void onBeforeInsert(T t) throws EntityBadInputException;

    /**
     * Method which created for check existence of gift certificate before using method of updating
     * it on bd.
     * @param t object of gift certificate class with params for updating. If does not exist,
     * then optional of null.
     * @param id of gift certificate.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    void onBeforeUpdate(Optional<T> t, K id) throws EntityNotFoundException;

    /**
     * Method which created for check params of searching filter of gift certificates.
     * @param giftSearchFilter need to set settings of searching gift certificates on bd.
     * @throws EntityBadInputException if one of filters params is incorrect.
     */
    void onBeforeFindAllEntities(GiftSearchFilter giftSearchFilter) throws EntityBadInputException;
}
