package com.epam.esm.service.gift;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.jdbc.gift.JdbcTemplateGiftDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.util.mapper.AbstractEntityMapper;
import com.epam.esm.util.validation.BeforeGiftValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiftServiceImpl implements GiftService{

    private final JdbcTemplateGiftDao jdbcTemplateGiftDao;
    private final AbstractEntityMapper<GiftCertificateDto, GiftCertificateDao> giftMapper;
    private final BeforeGiftValidation<GiftCertificateDto, Long> giftValidation;

    @Autowired
    public GiftServiceImpl(JdbcTemplateGiftDao jdbcTemplateGiftDao,
                           AbstractEntityMapper<GiftCertificateDto,
                                   GiftCertificateDao> giftCertificateMapper,
                           BeforeGiftValidation<GiftCertificateDto, Long> giftValidation) {
        this.jdbcTemplateGiftDao = jdbcTemplateGiftDao;
        this.giftMapper = giftCertificateMapper;
        this.giftValidation = giftValidation;
    }


    @Override
    @Transactional
    public List<GiftCertificateDto> findAllEntities(GiftSearchFilter giftSearchFilter) {
        giftValidation.onBeforeFindAllEntities(giftSearchFilter);
        return jdbcTemplateGiftDao.findAllEntities(giftSearchFilter)
                .stream()
                .map(giftMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<GiftCertificateDto> findEntityById(Long id) {
        Optional<GiftCertificateDto> giftCertificateDto = jdbcTemplateGiftDao.findEntityById(id)
                .map(giftMapper::toDto);

        giftValidation.onBeforeFindEntity(giftCertificateDto, id);
        return giftCertificateDto;
    }

    @Override
    @Transactional(rollbackFor = EntityBadInputException.class)
    public GiftCertificateDto create(GiftCertificateDto giftCertificateDto) {
        giftValidation.onBeforeInsert(giftCertificateDto);
        return giftMapper.toDto(jdbcTemplateGiftDao
                .create(giftMapper.toDao(giftCertificateDto)));
    }

    @Override
    @Transactional(rollbackFor = EntityNotFoundException.class)
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        giftValidation.onBeforeUpdate(jdbcTemplateGiftDao.findEntityById(giftCertificateDto.getId())
                .map(giftMapper::toDto),
                giftCertificateDto.getId());

        return giftMapper.toDto(jdbcTemplateGiftDao.update(giftMapper
                .toDao(giftCertificateDto)));
    }

    @Override
    public boolean delete(Long id) {
        giftValidation.onBeforeDelete(jdbcTemplateGiftDao.findEntityById(id)
                        .map(giftMapper::toDto).isPresent(), id);

        return jdbcTemplateGiftDao.delete(id);
    }
}
