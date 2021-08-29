package com.epam.esm.service.tag;

import com.epam.esm.service.dto.TagDto;

import java.util.List;
import java.util.Optional;

public interface TagService {

    List<TagDto> findAllEntities();

    Optional<TagDto> findEntityById(Long id);

    TagDto create(TagDto tagDto);

    boolean delete(Long id);

    Optional<TagDto> findTagByName(String name);
}
