package com.epam.esm.persistence.jdbc.gift;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.jdbc.JdbcTemplateBaseDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;

import java.util.List;

public interface JdbcTemplateGiftDao extends JdbcTemplateBaseDao<Long, GiftCertificateDao> {

    /**
     * Method created for searching all the gift certificates on db.
     * @param giftSearchFilter need to set search parameters.
     * @return list of found gift certificates.
     */
    List<GiftCertificateDao> findAllEntities(GiftSearchFilter giftSearchFilter);

    /**
     * Method created for update gift certificate on db.
     * @param giftCertificate gift certificate which we want update on db.
     * @return gift certificate after update.
     */
    GiftCertificateDao update(GiftCertificateDao giftCertificate);
}
