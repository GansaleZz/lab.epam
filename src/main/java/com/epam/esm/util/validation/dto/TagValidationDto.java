package com.epam.esm.util.validation.dto;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.validation.BeforeTagValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagValidationDto implements BeforeTagValidation<TagDto, Long> {

    @Override
    public void onBeforeDelete(Boolean isPresent, Long id) throws EntityNotFoundException {
        if (!isPresent) {
            throw new EntityNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeInsert(TagDto tagDto) throws EntityBadInputException {
        if (tagDto.getName() == null) {
            throw new EntityBadInputException("Please enter all the details");
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<TagDto> tagDto, Long id) throws EntityNotFoundException {
        if (!tagDto.isPresent()) {
            throw new EntityNotFoundException("Requested tag not found (id = "+id+")");
        }
    }

    @Override
    public void onBeforeFindEntity(Optional<TagDto> tagDto, String name) throws EntityNotFoundException {
        if (!tagDto.isPresent()) {
            throw new EntityNotFoundException("Requested tag not found (name = "+name+")");
        }
    }
}
