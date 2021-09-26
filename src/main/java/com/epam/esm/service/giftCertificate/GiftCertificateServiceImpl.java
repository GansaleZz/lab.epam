package com.epam.esm.service.giftCertificate;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private static final String NOT_FOUND = "Requested gift not found (id = %s)";
    private final GiftCertificateDao giftDao;
    private final AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper;

    @Autowired
    public GiftCertificateServiceImpl(GiftCertificateDao giftDao,
                                      AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper) {
        this.giftDao = giftDao;
        this.giftMapper = giftMapper;
    }

    @Override
    public List<GiftCertificateDto> findAllGiftCertificates(GiftCertificateSearchFilter giftSearchFilter,
                                                            PaginationFilter paginationFilter) {
        return giftDao.findAllGiftCertificates(giftSearchFilter, paginationFilter)
                .stream()
                .map(giftMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findGiftCertificateById(Long giftCertificateId) {
        return giftDao.findEntityById(giftCertificateId)
                .map(giftMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND, giftCertificateId)));
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        return giftMapper.toDto(giftDao
                .create(giftMapper.toEntity(giftCertificateDto)));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        giftDao.findEntityById(giftCertificateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND,
                        giftCertificateDto.getId())));

        return giftMapper.toDto(giftDao.update(giftMapper
                .toEntity(giftCertificateDto)));
    }

    @Override
    public boolean delete(Long giftCertificateId) {
        if (!giftDao.delete(giftCertificateId)) {
            throw new EntityNotFoundException(String.format(NOT_FOUND, giftCertificateId));
        } else {
            return true;
        }
    }
}
