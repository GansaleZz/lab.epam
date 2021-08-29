package com.epam.esm.util.validation.dao;

import com.epam.esm.persistence.dao.TagDao;
import com.epam.esm.util.validation.BeforeTagValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagValidationDao implements BeforeTagValidation<TagDao, Long> {

    @Override
    public void onBeforeDelete(Boolean isPresent, Long id) throws EntityNotFoundException {
        if (!isPresent) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<TagDao> tagDao, Long id) throws EntityNotFoundException {
        if (!tagDao.isPresent()) {
            throw new EntityNotFoundException("Requested tag not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<TagDao> tagDao, String name) throws EntityNotFoundException {
        if (!tagDao.isPresent()) {
            throw new EntityNotFoundException("Requested tag not found (name = "+name+")");
        }
    }

    @Override
    public void onBeforeInsert(TagDao tagDao) throws EntityBadInputException {
        if (tagDao.getName() == null) {
            throw new EntityBadInputException("Please enter all the details");
        }
    }
}
