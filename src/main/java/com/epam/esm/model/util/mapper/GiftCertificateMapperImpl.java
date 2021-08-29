package com.epam.esm.model.util.mapper;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;

public class GiftCertificateMapperImpl implements AbstractEntityMapper<GiftCertificateDto, GiftCertificate> {

    @Override
    public GiftCertificate toEntity(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = GiftCertificate.builder()
                .id(giftCertificateDto.getId())
                .name(giftCertificateDto.getName())
                .description(giftCertificateDto.getDescription())
                .duration(giftCertificateDto.getDuration())
                .createDate(giftCertificateDto.getCreateDate())
                .lastUpdateDate(giftCertificateDto.getLastUpdateDate()).build();
        if(giftCertificateDto.getTagName() != null){
            giftCertificate.setTagName(giftCertificateDto.getTagName());
        }
        return giftCertificate;
    }

    @Override
    public GiftCertificateDto toDto(GiftCertificate giftCertificate) {
        GiftCertificateDto giftCertificateDto = GiftCertificateDto.builder()
                .name(giftCertificate.getName())
                .description(giftCertificate.getDescription())
                .price(giftCertificate.getPrice())
                .duration(giftCertificate.getDuration())
                .createDate(giftCertificate.getCreateDate())
                .lastUpdateDate(giftCertificate.getLastUpdateDate()).build();
        if(giftCertificateDto.getTagName() != null){
            giftCertificateDto.setTagName(giftCertificate.getTagName());
        }
        return giftCertificateDto;
    }
}
