package com.epam.esm.service.tag;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.util.pagination.PageFilter;

import java.util.List;

public interface TagService {

    /**
     * Extracts all tags from db.
     * @return list of found tags.
     */
    List<TagDto> findAllTags(PageFilter paginationFilter);

    /**
     * Searching tag on db by id.
     * @param tagId unique parameter of tag, by which we can find it.
     * @return tag if it exists else throws exception.
     */
    TagDto findTagById(Long tagId);

    /**
     * Searching for the tag that most often occurs in orders of
     * user with the highest cost of all orders.
     * @return found tag.
     */
    TagDto findMostWidelyUsedTag();

    /**
     * Creating tag on db.
     * @param tagDto tag which we want create.
     * @return tag with id which was created.
     */
    TagDto createTag(TagDto tagDto);

    /**
     * Deleting tag from db.
     * @param tagId of tag which we want to delete from db.
     * @return returns true if tag was deleted, else returns false.
     */
    boolean deleteTag(Long tagId);
}
