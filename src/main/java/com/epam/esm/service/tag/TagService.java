package com.epam.esm.service.tag;

import com.epam.esm.service.dto.TagDto;

import java.util.List;

public interface TagService {

    /**
     * Extracts all tags from db.
     * @return list of found tags.
     */
    List<TagDto> findAllTags();

    /**
     * Searching tag on db by id.
     * @param id unique parameter of tag, by which we can find it.
     * @return tag if it exists else throws exception.
     */
    TagDto findTagById(Long id);

    /**
     * Creating tag on db.
     * @param tagDto - tag which we want create.
     * @return tag with id which was created.
     */
    TagDto create(TagDto tagDto);

    /**
     * Deleting tag from db.
     * @param id of tag which we want to delete from db.
     * @return returns true if tag was deleted, else returns false.
     */
    boolean delete(Long id);

    /**
     * Searching tag on db by its name.
     * @param name parameter of tag, by which we searching it.
     * @return tag if it exists else throws exception.
     */
    TagDto findTagByName(String name);
}
