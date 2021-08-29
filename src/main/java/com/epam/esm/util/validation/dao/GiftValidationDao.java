package com.epam.esm.util.validation.dao;

import com.epam.esm.persistence.dao.GiftCertificateDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.util.validation.BeforeGiftValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GiftValidationDao implements BeforeGiftValidation<GiftCertificateDao, Long> {

    @Override
    public void onBeforeDelete(Boolean isPresent, Long id) throws EntityNotFoundException{
        if (!isPresent) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<GiftCertificateDao> giftCertificateDao,
                                   Long id) throws EntityNotFoundException{
        if (!giftCertificateDao.isPresent()) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeInsert(GiftCertificateDao giftCertificateDao) throws EntityBadInputException{
        if (giftCertificateDao.getDescription() == null ||
                giftCertificateDao.getDuration().equals(0) ||
                giftCertificateDao.getName() == null ||
                giftCertificateDao.getPrice().equals(0.0)) {
            throw new EntityBadInputException("Please enter all the details correct");
        }
    }

    @Override
    public void onBeforeUpdate(Optional<GiftCertificateDao> giftCertificateDao,
                               Long id) throws EntityNotFoundException {
        if (!giftCertificateDao.isPresent()) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

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
}
