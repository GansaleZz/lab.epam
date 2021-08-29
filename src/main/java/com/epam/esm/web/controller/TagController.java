package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.util.validation.BeforeTagValidation;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private final BeforeTagValidation<TagDto, Long> tagValidation;

    @Autowired
    public TagController(TagService tagService, BeforeTagValidation<TagDto, Long> tagValidation) {
        this.tagService = tagService;
        this.tagValidation = tagValidation;
    }

    /**
     * Method which created to get all tags from bd using get HTTP method
     * by URI '/tags'.
     * @return list of tags on JSON format.
     */
    @GetMapping
    public ResponseEntity<List<TagDto>> listOfTags(){
        return new ResponseEntity<>(tagService.findAllEntities(), HttpStatus.OK);
    }

    /**
     * Method which created to get tag by id from bd using get HTTP method
     * by URI '/tags/{id}'.
     * @param id of tag.
     * @return found tag on JSON format.
     * @throws EntityNotFoundException if tag does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> tagById(@PathVariable("id") Long id) throws EntityNotFoundException {
        Optional<TagDto> tag = tagService.findEntityById(id);

        tagValidation.onBeforeFindEntity(tag, id);
        return new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    /**
     * Method which created to get tag by name from bd using get HTTP method
     * by URI '/tags/name/{name}'.
     * @param name of tag.
     * @return found tag on JSON format.
     * @throws EntityNotFoundException if tag does not exist.
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<TagDto> tagByName(@PathVariable("name") String name) throws EntityNotFoundException {
        Optional<TagDto> tag = tagService.findTagByName(name);

        tagValidation.onBeforeFindEntity(tag, name);
        return new ResponseEntity<>(tag.get(), HttpStatus.OK);
    }

    /**
     * Method which created to create tag on bd using post HTTP method
     * by URI '/tags'.
     * @param tag which we want to create on bd.
     * @return created tag on JSON format.
     * @throws EntityBadInputException if one of non null parameters is null.
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@RequestBody TagDto tag) throws EntityBadInputException {
        tagValidation.onBeforeInsert(tag);
        return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
    }

    /**
     * Method which created to delete tag from bd using delete HTTP method
     * by URI '/tags/.
     * @param id of tag.
     * @return result of deleting.
     * @throws EntityNotFoundException if tag does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) throws EntityNotFoundException{
        tagValidation.onBeforeDelete(tagService.delete(id), id);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
