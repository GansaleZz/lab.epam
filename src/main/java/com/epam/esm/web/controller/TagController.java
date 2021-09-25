package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationEntityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String TAG = "tag";
    private static final String TAGS = "tags";
    private static final String LIST_OF_TAGS = "listOfTags";
    private static final String BAD_INPUT = "Please enter correct details for ";
    private static final String NO_METHOD = "Method not found";
    private final TagService tagService;
    private final PaginationEntityLink paginationTagLink;

    @Autowired
    public TagController(TagService tagService, PaginationEntityLink paginationEntityLink) {
        this.tagService = tagService;
        this.paginationTagLink = paginationEntityLink;
    }

    /**
     * Extracts all tags from bd.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult - need for catch problems with validating.
     * @return list of tags on JSON format.
     */
    @GetMapping
    public CollectionModel<EntityModel<TagDto>> listOfTags(@Valid PaginationFilter paginationFilter,
                                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(BAD_INPUT + bindingResult.getFieldError().getField());
        }

        List<TagDto> tags = tagService.findAllTags(paginationFilter);
        List<EntityModel<TagDto>> model = tags.stream()
                .map(EntityModel::of).collect(Collectors.toList());
        Method listOfTags;
        try {
            listOfTags = this.getClass().getMethod(LIST_OF_TAGS,
                    PaginationFilter.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(NO_METHOD);
        }

        model.forEach(tag -> tag.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                                .tagById(tag.getContent().getId()))
                        .withRel(TAG)
                        .withName(tag.getContent().getName())));

        CollectionModel<EntityModel<TagDto>> result = CollectionModel.of(model);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .listOfTags(paginationFilter,
                                bindingResult))
                .slash(String.format(PaginationEntityLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withSelfRel();
        result.add(selfLink);

        if (paginationFilter.getItems() != 0) {
            paginationTagLink.nextLink(listOfTags, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationTagLink.prevLink(listOfTags, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationTagLink.firstLink(listOfTags, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationTagLink.lastLink(listOfTags, paginationFilter, bindingResult)
                    .ifPresent(result::add);
        }

        return result;
    }

    /**
     * Extracts tag by id from bd.
     * @param id - tag's id.
     * @return found tag on JSON format.
     */
    @GetMapping("/{id}")
    public EntityModel<TagDto> tagById(@PathVariable("id") Long id) {
        TagDto tag = tagService.findTagById(id);
        EntityModel<TagDto> model = EntityModel.of(tag);
        PaginationFilter paginationFilter = PaginationFilter.builder().build();

        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .tagById(id))
                .withSelfRel();

        Link tagsLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .listOfTags(paginationFilter,
                                null))
                .slash(String.format(PaginationEntityLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withRel(TAGS);
        model.add(selfLink, tagsLink);

        return model;
    }

    /**
     * Creating tag on bd.
     * @param tag which we want to create on bd.
     * @param bindingResult - need for catch problems with validating.
     * @return created tag on JSON format.
     */
    @PostMapping
    public ResponseEntity<TagDto> create(@Valid @RequestBody TagDto tag,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(BAD_INPUT + bindingResult.getFieldError().getField());
        }

        return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
    }

    /**
     * Deleting tag from bd.
     * @param id - tag's id.
     * @return result of deleting.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tagService.delete(id), HttpStatus.OK);
    }
}
