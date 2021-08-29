package com.epam.esm.util.validation.dto;

import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.util.validation.BeforeGiftValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftValidationDto implements BeforeGiftValidation<GiftCertificateDto, Long> {

    @Override
    public void onBeforeFindAllEntities(GiftSearchFilter giftSearchFilter) throws EntityBadInputException {
        if (giftSearchFilter.getGiftsByDateOrder() == null) {
            throw new EntityBadInputException("Please enter correct details for 'getGiftsByDateOrder'");
        } else {
            if (giftSearchFilter.getGiftsByNameOrder() == null) {
                throw new EntityBadInputException("Please enter correct details for 'getGiftsByNameOrder'");
            }
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<GiftCertificateDto> giftCertificateDto,
                                   Long id) throws EntityNotFoundException {
        if (!giftCertificateDto.isPresent()) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeDelete(Boolean isPresent, Long id) throws EntityNotFoundException {
        if (!isPresent) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeInsert(GiftCertificateDto giftCertificateDto) throws EntityBadInputException {
        if (giftCertificateDto.getDescription() == null ||
                giftCertificateDto.getDuration().equals(0) ||
                giftCertificateDto.getName() == null ||
                giftCertificateDto.getPrice().equals(0.0)) {
            throw new EntityBadInputException("Please enter all the details correct");
        }
    }

    @Override
    public void onBeforeUpdate(Optional<GiftCertificateDto> giftCertificateDto,
                               Long id) throws EntityNotFoundException {
        if (!giftCertificateDto.isPresent()) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }
}
