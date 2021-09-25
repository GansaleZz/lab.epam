package com.epam.esm.service.giftCertificate;

import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.web.util.pagination.PaginationFilter;

import java.util.List;

public interface GiftCertificateService {

    /**
     * Extracts all gift certificates from db.
     * @param giftSearchFilter need to set search parameters.
     * @return list of found gift certificates.
     */
    List<GiftCertificateDto> findAllGifts(GiftCertificateSearchFilter giftSearchFilter, PaginationFilter paginationFilter);

    /**
     * Searching gift certificate on db by id.
     * @param id unique parameter of gift certificate, by which we can find it.
     * @return gift certificate if it exists else throws exception.
     */
    GiftCertificateDto findGiftById(Long id);

    /**
     * Creating gift certificate on db.
     * @param giftCertificateDto - certificate which we want create.
     * @return gift certificate with id which was created.
     */
    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    /**
     * Deleting gift certificate from db.
     * @param id of gift certificate which we want to delete from db.
     * @return returns true if gift certificate was deleted, else returns false.
     */
    boolean delete(Long id);

    /**
     * Updating gift certificate on db.
     * @param giftCertificateDto which we want update on db.
     * @return gift certificate after update.
     */
    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);
}
