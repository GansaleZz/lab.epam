package com.epam.esm.service.util.mapper;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GiftCertificateMapper implements AbstractEntityMapper<GiftCertificateDto, GiftCertificate> {

    @Autowired
    private AbstractEntityMapper<TagDto, Tag> tagMapper;

    @Override
    public GiftCertificate toEntity(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificateDao = GiftCertificate.builder()
                .giftId(giftCertificateDto.getId())
                .name(giftCertificateDto.getName())
                .description(giftCertificateDto.getDescription())
                .price(giftCertificateDto.getPrice())
                .duration(giftCertificateDto.getDuration())
                .createDate(giftCertificateDto.getCreateDate())
                .lastUpdateDate(giftCertificateDto.getLastUpdateDate())
                .build();
        if (giftCertificateDto.getTags() != null) {
            giftCertificateDto.getTags().forEach(tagDto -> giftCertificateDao
                    .getTags().add(tagMapper.toEntity(tagDto)));
        }
        return giftCertificateDao;
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate giftCertificateDao) {
         GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                 .id(giftCertificateDao.getGiftId())
                 .name(giftCertificateDao.getName())
                 .description(giftCertificateDao.getDescription())
                 .price(giftCertificateDao.getPrice())
                 .duration(giftCertificateDao.getDuration())
                 .createDate(giftCertificateDao.getCreateDate())
                 .lastUpdateDate(giftCertificateDao.getLastUpdateDate())
                 .build();
         if (giftCertificateDao.getTags() != null ) {
             giftCertificateDao.getTags().forEach(tagDao -> giftCertificateDto
                     .getTags().add(tagMapper.toDto(tagDao)));
         }
         return giftCertificateDto;
    }
}
