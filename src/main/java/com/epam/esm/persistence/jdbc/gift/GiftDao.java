package com.epam.esm.persistence.jdbc.gift;

import com.epam.esm.persistence.dao.GiftCertificate;
import com.epam.esm.persistence.jdbc.BaseDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;

import java.util.List;

public interface GiftDao extends BaseDao<Long, GiftCertificate> {

    /**
     * Searching all the gift certificates on db.
     * @param giftSearchFilter need to set search parameters.
     * @return list of found gift certificates.
     */
    List<GiftCertificate> findAllEntities(GiftSearchFilter giftSearchFilter);

    /**
     * Updating gift certificate on db.
     * @param giftCertificate gift certificate which we want update on db.
     * @return gift certificate after update.
     */
    GiftCertificate update(GiftCertificate giftCertificate);
}
