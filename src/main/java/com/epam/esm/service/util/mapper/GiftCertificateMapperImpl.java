package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.service.dto.GiftCertificateDto;
import org.springframework.stereotype.Component;


@Component
public class GiftCertificateMapperImpl implements AbstractEntityMapper<GiftCertificateDto, GiftCertificateDao> {

    @Override
    public GiftCertificateDao toDao(GiftCertificateDto giftCertificateDto) {
        GiftCertificateDao giftCertificateDao = GiftCertificateDao.builder()
                .id(giftCertificateDto.getId())
                .name(giftCertificateDto.getName())
                .description(giftCertificateDto.getDescription())
                .price(giftCertificateDto.getPrice())
                .duration(giftCertificateDto.getDuration())
                .createDate(giftCertificateDto.getCreateDate())
                .lastUpdateDate(giftCertificateDto.getLastUpdateDate())
                .build();
        giftCertificateDto.getTags().forEach(tagDto -> giftCertificateDao
                .addTag(new TagMapperImpl().toDao(tagDto)));
        return giftCertificateDao;
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificateDao giftCertificateDao) {
         GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                 .id(giftCertificateDao.getId())
                 .name(giftCertificateDao.getName())
                 .description(giftCertificateDao.getDescription())
                 .price(giftCertificateDao.getPrice())
                 .duration(giftCertificateDao.getDuration())
                 .createDate(giftCertificateDao.getCreateDate())
                 .lastUpdateDate(giftCertificateDao.getLastUpdateDate())
                 .build();
         giftCertificateDao.getTags().forEach(tagDao -> giftCertificateDto
         .addTag(new TagMapperImpl().toDto(tagDao)));
         return giftCertificateDto;
    }
}
