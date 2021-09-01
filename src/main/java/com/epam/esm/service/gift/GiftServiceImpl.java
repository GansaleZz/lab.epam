package com.epam.esm.service.gift;

import com.epam.esm.persistence.dao.GiftCertificate;
import com.epam.esm.persistence.jdbc.gift.GiftDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.util.validation.BaseGiftValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GiftServiceImpl implements GiftService{

    private static final String NOT_FOUND = "Requested gift not found (id = %s)";
    private final GiftDao jdbcTemplateGiftDao;
    private final AbstractEntityMapper<GiftCertificateDto, GiftCertificate> giftMapper;
    private final BaseGiftValidator<GiftCertificateDto, Long> giftValidation;

    @Autowired
    public GiftServiceImpl(GiftDao jdbcTemplateGiftDao,
                           AbstractEntityMapper<GiftCertificateDto,
                                   GiftCertificate> giftCertificateMapper,
                           BaseGiftValidator<GiftCertificateDto, Long> giftValidation) {
        this.jdbcTemplateGiftDao = jdbcTemplateGiftDao;
        this.giftMapper = giftCertificateMapper;
        this.giftValidation = giftValidation;
    }


    @Override
    @Transactional
    public List<GiftCertificateDto> findAllGifts(GiftSearchFilter giftSearchFilter) {
        giftValidation.onBeforeFindAllEntities(giftSearchFilter);
        return jdbcTemplateGiftDao.findAllEntities(giftSearchFilter)
                .stream()
                .map(giftMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public GiftCertificateDto findGiftById(Long id) {
        return jdbcTemplateGiftDao.findEntityById(id)
                .map(giftMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        giftValidation.onBeforeInsert(giftCertificateDto);
        return giftMapper.toDto(jdbcTemplateGiftDao
                .create(giftMapper.toEntity(giftCertificateDto)));
    }

    @Override
    @Transactional
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        jdbcTemplateGiftDao.findEntityById(giftCertificateDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND,
                        giftCertificateDto.getId())));

        return giftMapper.toDto(jdbcTemplateGiftDao.update(giftMapper
                .toEntity(giftCertificateDto)));
    }

    @Override
    public boolean delete(Long id) {
        if (!jdbcTemplateGiftDao.delete(id)) {
            throw new EntityNotFoundException(String.format(NOT_FOUND, id));
        } else {
            return true;
        }
    }
}
