package com.epam.esm.service.gift;

import com.epam.esm.persistence.entity.GiftCertificate;
import com.epam.esm.persistence.dao.GiftDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftServiceImpl implements GiftService{

    private static final String NOT_FOUND = "Requested gift not found (id = %s)";
    private final GiftDao giftDao;
    private final AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper;

    @Autowired
    public GiftServiceImpl(GiftDao giftDao,
                           AbstractEntityMapper<GiftCertificateDto,
                                   GiftCertificate> giftCertificateMapper) {
        this.giftDao = giftDao;
        this.giftMapper = giftCertificateMapper;
    }


    @Override
    public List<GiftCertificateDto> findAllGifts(GiftSearchFilter giftSearchFilter) {
        return giftDao.findAllEntities(giftSearchFilter)
                .stream()
                .map(giftMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findGiftById(Long id) {
        return giftDao.findEntityById(id)
                .map(giftMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND, id)));
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
    public boolean delete(Long id) {
        if (!giftDao.delete(id)) {
            throw new EntityNotFoundException(String.format(NOT_FOUND, id));
        } else {
            return true;
        }
    }
}
