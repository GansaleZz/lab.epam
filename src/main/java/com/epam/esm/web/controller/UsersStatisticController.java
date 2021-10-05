package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.util.pagination.PageFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersStatisticController {

    private static final String TAGS = "tags";
    private final TagService tagService;

    @Autowired
    public UsersStatisticController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Searching for the tag that most often occurs in orders of
     * user with the highest cost of all orders.
     * @return found tag.
     */
    @GetMapping("/mostWidelyUsedTag")
    public EntityModel<TagDto> retrieveMostWidelyUsedTag() {
        TagDto tagDto = tagService.findMostWidelyUsedTag();

        EntityModel<TagDto> model = EntityModel.of(tagDto);
        Link selfLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
                        .methodOn(this.getClass())
                        .retrieveMostWidelyUsedTag()
                )
                .withSelfRel();

        model.add(selfLink);
        model.add(TagController.getAllTags(PageFilter.builder().build(), null)
                .withRel(TAGS));

        return model;
    }
}
