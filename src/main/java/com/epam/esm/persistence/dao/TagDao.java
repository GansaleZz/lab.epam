package com.epam.esm.persistence.dao;

import com.epam.esm.model.dto.TagDto;

import java.util.Optional;

public interface TagDao extends BaseDao<Integer, TagDto> {

    Optional<TagDto> findTagByName(String name);

}
