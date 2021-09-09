package com.epam.esm.util.validation.entity;

import com.epam.esm.persistence.entity.Tag;
import com.epam.esm.util.validation.BaseTagValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import org.springframework.stereotype.Component;

@Component
public class TagValidator implements BaseTagValidator<Tag, Long> {

    private static final String BAD_INPUT_INSERT = "Please enter all the details";

    @Override
    public void onBeforeInsert(Tag tagDao) throws EntityBadInputException {
        if (tagDao.getName() == null) {
            throw new EntityBadInputException(BAD_INPUT_INSERT);
        }
    }
}
