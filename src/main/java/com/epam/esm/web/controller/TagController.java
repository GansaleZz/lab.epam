package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Extracts all tags from bd.
     * @return list of tags on JSON format.
     */
    @GetMapping
    public ResponseEntity<List<TagDto>> listOfTags(){
        return new ResponseEntity<>(tagService.findAllTags(), HttpStatus.OK);
    }

    /**
     * Extracts tag by id from bd.
     * @param id of tag.
     * @return found tag on JSON format.
     * @throws EntityNotFoundException if tag does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> tagById(@PathVariable("id") Long id) throws EntityNotFoundException {
        TagDto tag = tagService.findTagById(id);

        return new ResponseEntity<>(tag, HttpStatus.OK);
    }

    /**
     * Creating tag on bd.
     * @param tag which we want to create on bd.
     * @return created tag on JSON format.
     * @throws EntityBadInputException if one of non null parameters is null.
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody TagDto tag, BindingResult error) throws EntityBadInputException {
        if (error.hasErrors()) {
            throw new EntityBadInputException(error.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
    }

    /**
     * Deleting tag from bd.
     * @param id of tag.
     * @return result of deleting.
     * @throws EntityNotFoundException if tag does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) throws EntityNotFoundException{
        return new ResponseEntity<>(tagService.delete(id), HttpStatus.OK);
    }
}
