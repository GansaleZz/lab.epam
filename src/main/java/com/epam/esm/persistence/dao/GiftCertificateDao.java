package com.epam.esm.persistence.dao;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface GiftCertificateDao extends BaseDao<Long, GiftCertificate> {

    /**
     * Searching all the gift certificates on db.
     * @param giftSearchFilter need to set search parameters.
     * @return list of found gift certificates.
     */
    List<GiftCertificate> findAllEntities(GiftCertificateSearchFilter giftSearchFilter, PaginationFilter paginationFilter);

    /**
     * Updating gift certificate on db.
     * @param giftCertificate gift certificate which we want update on db.
     * @return gift certificate after update.
     */
    GiftCertificate update(GiftCertificate giftCertificate);
}
