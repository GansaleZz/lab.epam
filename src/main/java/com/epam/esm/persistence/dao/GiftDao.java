package com.epam.esm.persistence.dao;

import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;

import java.util.List;

public interface GiftDao extends BaseDao<Integer, GiftCertificateDto>{

    boolean update(GiftCertificateDto giftCertificate);

    boolean update(GiftCertificateDto giftCertificate, TagDto tag);

    boolean create(GiftCertificateDto giftCertificate, TagDto tag);

    List<GiftCertificateDto> findGiftsByName(String name);

    List<GiftCertificateDto> findGiftsByDescription(String description);

    List<GiftCertificateDto> findGiftsByTag(TagDto tag);

    List<GiftCertificateDto> sortGiftsByNameASC();

    List<GiftCertificateDto> sortGiftsByNameDESC();

    List<GiftCertificateDto> sortGiftsByDescriptionASC();

    List<GiftCertificateDto> sortGiftsByDescriptionDESC();
}
