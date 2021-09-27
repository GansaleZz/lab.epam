package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpecialController {

    private final TagService tagService;

    @Autowired
    public SpecialController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Searching the most widely used tag of a user with the highest cost of all orders on db.
     * @return found tag.
     */
    @GetMapping("/mostWidelyUsedTag")
    public EntityModel<TagDto> mostWidelyUsedTag() {
        TagDto tagDto = tagService.findMostWidelyUsedTag();

        EntityModel<TagDto> model = EntityModel.of(tagDto);
        Link selfLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
                        .methodOn(this.getClass())
                        .mostWidelyUsedTag()
                )
                .withSelfRel();

        model.add(selfLink);

        return model;
    }
}
