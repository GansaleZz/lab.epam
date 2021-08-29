package com.epam.esm.service.gift;

import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;

import java.util.List;
import java.util.Optional;

public interface GiftService{

    List<GiftCertificateDto> findAllEntities(GiftSearchFilter giftSearchFilter);

    Optional<GiftCertificateDto> findEntityById(Long id);

    GiftCertificateDto create(GiftCertificateDto giftCertificateDto);

    boolean delete(Long id);

    GiftCertificateDto update(GiftCertificateDto t);
}
