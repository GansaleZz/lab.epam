package com.epam.esm.util.validation.entity;

import com.epam.esm.persistence.dao.GiftCertificate;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.util.validation.BaseGiftValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import org.springframework.stereotype.Component;

@Component
public class GiftValidator implements BaseGiftValidator<GiftCertificate, Long> {

    private static final String BAD_INPUT_INSERT = "Please enter all the details correct";
    private static final String BAD_INPUT_DATE_ORDER = "Please enter correct details for 'getGiftsByDateOrder'";
    private static final String BAD_INPUT_NAME_ORDER = "Please enter correct details for 'getGiftsByNameOrder'";

    @Override
    public void onBeforeInsert(GiftCertificate giftCertificateDao) throws EntityBadInputException{
        if (giftCertificateDao.getDescription() == null ||
                giftCertificateDao.getDuration().equals(0) ||
                giftCertificateDao.getName() == null ||
                giftCertificateDao.getPrice().equals(0.0)) {
            throw new EntityBadInputException(BAD_INPUT_INSERT);
        }
    }

    @Override
    public void onBeforeFindAllEntities(GiftSearchFilter giftSearchFilter) throws EntityBadInputException {
        if (giftSearchFilter.getGiftsByDateOrder() == null) {
            throw new EntityBadInputException(BAD_INPUT_DATE_ORDER);
        } else {
            if (giftSearchFilter.getGiftsByNameOrder() == null) {
                throw new EntityBadInputException(BAD_INPUT_NAME_ORDER);
            }
        }
    }
}
