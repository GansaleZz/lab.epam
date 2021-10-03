package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PageFilter;
import com.epam.esm.web.util.pagination.PaginationEntityLinkHelper;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tags")
public class TagController {

    private static final String TAG = "tag";
    private static final String TAGS = "tags";
    private final TagService tagService;
    private final ControllerHelper<TagDto> controllerHelper;

    @Autowired
    public TagController(TagService tagService, ControllerHelper<TagDto> controllerHelper) {
        this.tagService = tagService;
        this.controllerHelper = controllerHelper;
    }

    /**
     * Extracts all tags from bd.
     * @param pageFilter object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult need for catch problems with validating.
     * @return list of tags on JSON format.
     */
    @GetMapping
    public CollectionModel<EntityModel<TagDto>> retrieveAllTags(@Valid PageFilter pageFilter,
                                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        List<TagDto> tags = tagService.findAllTags(pageFilter);
        List<EntityModel<TagDto>> model = tags.stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        model.forEach(tag -> tag.add(getTagByIdLink(tag.getContent().getId())
                .withName(tag.getContent().getName())
                .withRel(TAG)));

        CollectionModel<EntityModel<TagDto>> result = CollectionModel.of(model);

        Link selfLink = getAllTags(pageFilter, bindingResult)
                .withSelfRel();

        result.add(selfLink);

        controllerHelper.retrievePaginationLinks(pageFilter, selfLink, result);

        return result;
    }

    /**
     * Extracts tag by id from bd.
     * @param id tag's id.
     * @return found tag on JSON format.
     */
    @GetMapping("/{id}")
    public EntityModel<TagDto> retrieveTagById(@PathVariable("id") Long id) {
        TagDto tag = tagService.findTagById(id);
        EntityModel<TagDto> model = EntityModel.of(tag);

        model.add(getTagByIdLink(id).withSelfRel());
        model.add(getAllTags(PageFilter.builder().build(), null)
                .withRel(TAGS));

        return model;
    }

    /**
     * Creating tag on bd.
     * @param tag which we want to create on bd.
     * @param bindingResult need for catching problems with validating.
     * @return created tag on JSON format.
     */
    @PostMapping
    public ResponseEntity<TagDto> createTag(@Valid @RequestBody TagDto tag,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        return new ResponseEntity<>(tagService.createTag(tag), HttpStatus.CREATED);
    }

    /**
     * Deleting tag from bd.
     * @param id tag's id.
     * @return result of deleting.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteTag(@PathVariable("id") Long id) {
        return new ResponseEntity<>(tagService.deleteTag(id), HttpStatus.OK);
    }

    static Link getTagByIdLink(Long tagId) {
        return Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .retrieveTagById(tagId))
                .toString());
    }

    static Link getAllTags(PageFilter pageFilter,
                           BindingResult bindingResult) {

        Link link = Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(TagController.class)
                        .retrieveAllTags(pageFilter, bindingResult))
                .toString());

        return Link.of(UriComponentsBuilder.fromUri(link.toUri())
                .queryParams(PaginationEntityLinkHelper.getPagingParameters(pageFilter))
                .build(true)
                .toString());
    }
}
