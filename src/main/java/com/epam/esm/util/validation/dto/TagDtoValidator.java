package com.epam.esm.util.validation.dto;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.util.validation.BaseTagValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import org.springframework.stereotype.Component;

@Component
public class TagDtoValidator implements BaseTagValidator<TagDto, Long> {

    private static final String BAD_INPUT_INSERT = "Please enter all the details";

    @Override
    public void onBeforeInsert(TagDto tagDto) throws EntityBadInputException {
        if (tagDto.getName() == null) {
            throw new EntityBadInputException(BAD_INPUT_INSERT);
        }
    }
}
