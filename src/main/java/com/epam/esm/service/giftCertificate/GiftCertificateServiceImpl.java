package com.epam.esm.service.giftCertificate;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftCertificateMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftDao,
                                      AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper) {
        this.giftCertificateDao = giftDao;
        this.giftCertificateMapper = giftMapper;
    }

    @Override
    public List<GiftCertificateDto> findAllGiftCertificates(GiftCertificateSearchFilter giftCertificateSearchFilter,
                                                            PageFilter paginationFilter) {
        return giftCertificateDao.findAllGiftCertificates(giftCertificateSearchFilter, paginationFilter)
                .stream()
                .map(giftCertificateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findGiftCertificateById(Long giftCertificateId) {
        return giftCertificateDao.findEntityById(giftCertificateId)
                .map(giftCertificateMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(giftCertificateId.toString()));
    }

    @Override
    @Transactional
    public GiftCertificateDto createGiftCertificate(GiftCertificateDto giftCertificateDto) {
        return giftCertificateMapper.toDto(giftCertificateDao
                .createEntity(giftCertificateMapper.toEntity(giftCertificateDto)));
    }

    @Override
    @Transactional
    public GiftCertificateDto updateGiftCertificate(GiftCertificateDto giftCertificateDto) {
        return giftCertificateMapper
                .toDto(giftCertificateDao.updateGiftCertificate(giftCertificateMapper.toEntity(giftCertificateDto)));
    }

    @Override
    public boolean deleteGiftCertificate(Long giftCertificateId) {
        return giftCertificateDao.deleteEntity(giftCertificateId);
    }
}
