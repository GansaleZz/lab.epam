package com.epam.esm.web.controller;

import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.giftCertificate.GiftCertificateService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.exception.EntityNotFoundException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationGiftCertificateLink;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private static final String NO_METHOD = "Method not found";
    private static final String BAD_INPUT = "Please enter correct details for ";
    private static final String GIFT_CERTIFICATE = "gift-certificate";
    private static final String GIFT_CERTIFICATES = "gift-certificates";
    private static final String LIST_OF_GIFT_CERTIFICATES = "listOfGiftCertificates";
    private static final String TAG = "tag";
    private final GiftCertificateService giftService;
    private final PaginationGiftCertificateLink paginationGiftCertificateLink;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftService,
                                     PaginationGiftCertificateLink paginationGiftCertificateLink) {
        this.giftService = giftService;
        this.paginationGiftCertificateLink = paginationGiftCertificateLink;
    }

    /**
     * Extracts all gift certificates from bd.
     * @return found list of gift certificates on JSON format.
     * @throws EntityBadInputException if one of filters fields is incorrect.
     */
    @GetMapping
    public CollectionModel<EntityModel<GiftCertificateDto>> listOfGiftCertificates(PaginationFilter paginationFilter,
                                                                        @Valid GiftCertificateSearchFilter giftSearchFilter,
                                                                        BindingResult bindingResult)
            throws EntityBadInputException, NoSuchMethodException {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(BAD_INPUT + bindingResult.getFieldError().getField());
        }

        List<GiftCertificateDto> certificates = giftService.findAllGifts(giftSearchFilter, paginationFilter);
        List<EntityModel<GiftCertificateDto>> model = certificates.stream()
                .map(EntityModel::of).collect(Collectors.toList());
        Method listOfGiftCertificates = this.getClass().getMethod(LIST_OF_GIFT_CERTIFICATES,
                PaginationFilter.class,
                GiftCertificateSearchFilter.class,
                BindingResult.class);

        model.forEach(
                certificate -> {
                    List<Link> tagLinks = new ArrayList<>();
                    certificate.getContent().getTags()
                            .forEach(
                                    tag -> {
                                        try {
                                            tagLinks.add(WebMvcLinkBuilder
                                                    .linkTo(WebMvcLinkBuilder
                                                            .methodOn(TagController.class)
                                                            .tagById(tag.getId()))
                                                    .withRel(TAG )
                                                    .withName(tag.getName()));
                                        } catch (NoSuchMethodException e) {
                                            throw new RuntimeException(NO_METHOD);
                                        }
                                    }
                            );
                    try {
                        certificate.add(
                                WebMvcLinkBuilder
                                        .linkTo(WebMvcLinkBuilder
                                                .methodOn(this.getClass())
                                                .giftCertificateById(certificate.getContent().getId()))
                                        .withRel(GIFT_CERTIFICATE)
                                );
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(NO_METHOD);
                    }
                    tagLinks.forEach(certificate::add);
                });

        CollectionModel<EntityModel<GiftCertificateDto>> result =
                CollectionModel.of(model);

        result.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(this.getClass())
                        .listOfGiftCertificates(paginationFilter,
                                giftSearchFilter,
                                bindingResult)
                )
                .slash(String.format(PaginationGiftCertificateLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withSelfRel());

        if (paginationFilter.getItems() != 0) {
            paginationGiftCertificateLink.nextLink(listOfGiftCertificates,
                            paginationFilter,
                            giftSearchFilter,
                            bindingResult)
                    .ifPresent(result::add);
            paginationGiftCertificateLink.prevLink(listOfGiftCertificates,
                            paginationFilter,
                            giftSearchFilter,
                            bindingResult)
                    .ifPresent(result::add);
            paginationGiftCertificateLink.firstLink(listOfGiftCertificates,
                            paginationFilter,
                            giftSearchFilter,
                            bindingResult)
                    .ifPresent(result::add);
            paginationGiftCertificateLink.lastLink(listOfGiftCertificates,
                            paginationFilter,
                            giftSearchFilter,
                            bindingResult)
                    .ifPresent(result::add);
        }

        return result;
    }

    /**
     * Extracts gift certificate by id from bd.
     * @param id of gift certificate.
     * @return gift certificate (if exists) on JSON format.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    @GetMapping(value = "/{id}")
    public EntityModel<GiftCertificateDto> giftCertificateById(@PathVariable("id") Long id) throws NoSuchMethodException {
        GiftCertificateDto giftCertificateDto = giftService.findGiftById(id);
        EntityModel<GiftCertificateDto> model = EntityModel.of(giftCertificateDto);
        List<TagDto> tags = giftCertificateDto.getTags();
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(0)
                .items(1)
                .build();

        Link selfLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .giftCertificateById(id)
                )
                .withSelfRel();
        Link certificatesLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .listOfGiftCertificates(new PaginationFilter(),
                                        new GiftCertificateSearchFilter(), null)
                )
                .slash(String.format(PaginationGiftCertificateLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withRel(GIFT_CERTIFICATES);

        model.add(selfLink,certificatesLink);

        tags.forEach(tag -> {
            Link link;
            try {
                link = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder
                                .methodOn(TagController.class).tagById(tag.getId()))
                        .withRel(TAG )
                        .withName(tag.getName());
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(NO_METHOD);
            }
            model.add(link);
        });

        return model;
    }

    /**
     * Creating gift certificate on bd.
     * @param dto gift certificate, which we want create on bd.
     * @return created gift certificate on JSON format.
     * @throws EntityBadInputException if one of params is incorrect.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@Valid @RequestBody GiftCertificateDto dto,
                                                     BindingResult error) {
        if (error.hasErrors()) {
            throw new EntityBadInputException(error.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(giftService.create(dto), HttpStatus.CREATED);
    }

    /**
     * Updating gift certificate on bd.
     * @param id of gift certificate.
     * @param dto params of gift certificate which we want to update.
     * @return updated gift certificate on JSON format.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable("id") Long id,
                                                     @RequestBody GiftCertificateDto dto ) {
        dto.setId(id);
        return new ResponseEntity<>(giftService.update(dto), HttpStatus.OK);
    }

    /**
     * Deleting gift certificate from bd.
     * @param id of gift certificate.
     * @return result of deleting on JSON format.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        return new ResponseEntity<>(giftService.delete(id), HttpStatus.OK);
    }
}