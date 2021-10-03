package com.epam.esm.web.controller;

import com.epam.esm.persistence.util.search.GiftCertificateSearchFilter;
import com.epam.esm.persistence.util.search.QueryOrder;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.giftCertificate.GiftCertificateService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/gift-certificates")
public class GiftCertificateController {

    private static final String GIFT_CERTIFICATE = "giftCertificate";
    private static final String GIFT_CERTIFICATES = "giftCertificates";
    private static final String GIFT_CERTIFICATE_NAME = "giftCertificateName";
    private static final String GIFT_CERTIFICATE_DESCRIPTION = "giftCertificateDescription";
    private static final String GIFT_CERTIFICATES_BY_DATE_ORDER = "giftCertificatesByDateOrder";
    private static final String GIFT_CERTIFICATES_BY_NAME_ORDER = "giftCertificatesByNameOrder";
    private static final String TAGS = "tags";
    private static final String TAG = "tag";
    private final GiftCertificateService giftService;
    private final ControllerHelper<GiftCertificateDto> controllerHelper;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftService,
                                     ControllerHelper<GiftCertificateDto> controllerHelper) {
        this.giftService = giftService;
        this.controllerHelper = controllerHelper;
    }

    /**
     * Extracts all gift certificates from bd.
     * @param pageFilter object which contains information about page's number
     *                         and number of items for paging.
     * @param giftCertificateSearchFilter need for set search parameters.
     * @param bindingResult need for catch problems with validating.
     * @return found list of gift certificates on JSON format.
     */
    @GetMapping
    public CollectionModel<EntityModel<GiftCertificateDto>> retrieveAllOfGiftCertificates(PageFilter pageFilter,
                                                                                          BindingResult bindingResult,
                                                                                          @Valid GiftCertificateSearchFilter giftCertificateSearchFilter) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        List<GiftCertificateDto> certificates = giftService
                .findAllGiftCertificates(giftCertificateSearchFilter, pageFilter);
        List<EntityModel<GiftCertificateDto>> model = certificates.stream()
                .map(EntityModel::of)
                .collect(Collectors.toList());

        model.forEach(
                certificate -> {
                    List<Link> tagLinks = new ArrayList<>();
                    certificate.getContent().getTags()
                            .forEach(tag -> tagLinks.add(TagController.getTagByIdLink(tag.getId())
                                    .withName(tag.getName())
                                    .withRel(TAG)));
                    certificate.add(getGiftCertificateByIdLink(certificate.getContent().getId())
                            .withRel(GIFT_CERTIFICATE));
                    tagLinks.forEach(certificate::add);
                });

        CollectionModel<EntityModel<GiftCertificateDto>> result = CollectionModel.of(model);
        Link selfLink = getAllGiftCertificatesLink(pageFilter,
                bindingResult,
                giftCertificateSearchFilter)
                .withSelfRel();

        result.add(selfLink);

        controllerHelper.retrievePaginationLinks(pageFilter,
                selfLink,
                result);

        return result;
    }

    /**
     * Extracts gift certificate by id from bd.
     * @param id gift certificate's id.
     * @return gift certificate (if exists) on JSON format.
     */
    @GetMapping(value = "/{id}")
    public EntityModel<GiftCertificateDto> retrieveGiftCertificateById(@PathVariable("id") Long id) {
        GiftCertificateDto giftCertificateDto = giftService.findGiftCertificateById(id);
        EntityModel<GiftCertificateDto> model = EntityModel.of(giftCertificateDto);
        List<TagDto> tags = giftCertificateDto.getTags();

        model.add(getAllGiftCertificatesLink(PageFilter.builder().build(),
                null,
                GiftCertificateSearchFilter.builder().build())
                .withRel(GIFT_CERTIFICATES));
        model.add(getGiftCertificateByIdLink(id).withSelfRel());

        tags.forEach(tag -> model.add(TagController.getTagByIdLink(tag.getId())
                .withName(tag.getName())
                .withRel(TAGS)));

        return model;
    }

    /**
     * Creating gift certificate on bd.
     * @param giftCertificateDto gift certificate, which we want create on bd.
     * @param bindingResult needs for catch problems with validating.
     * @return created gift certificate on JSON format.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> createGiftCertificate(@Valid @RequestBody GiftCertificateDto giftCertificateDto,
                                                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        return new ResponseEntity<>(giftService.createGiftCertificate(giftCertificateDto), HttpStatus.CREATED);
    }

    /**
     * Updating gift certificate on bd.
     * @param giftCertificateDto parameters of gift certificate which we want to update.
     * @return updated gift certificate on JSON format.
     */
    @PatchMapping
    public ResponseEntity<GiftCertificateDto> updateGiftCertificate(@RequestBody GiftCertificateDto giftCertificateDto ) {
        return new ResponseEntity<>(giftService.updateGiftCertificate(giftCertificateDto), HttpStatus.OK);
    }

    /**
     * Deleting gift certificate from bd.
     * @param id of gift certificate.
     * @return result of deleting on JSON format.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGiftCertificate(@PathVariable("id") Long id) {
        return new ResponseEntity<>(giftService.deleteGiftCertificate(id), HttpStatus.OK);
    }

    static Link getGiftCertificateByIdLink(Long giftCertificateId) {
        return Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GiftCertificateController.class)
                        .retrieveGiftCertificateById(giftCertificateId))
                .toString());
    }

    static Link getAllGiftCertificatesLink(PageFilter pageFilter,
                                            BindingResult bindingResult,
                                            GiftCertificateSearchFilter giftCertificateSearchFilter) {
        Link link = Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                        .methodOn(GiftCertificateController.class)
                        .retrieveAllOfGiftCertificates(pageFilter, bindingResult, giftCertificateSearchFilter))
                .toString());

        return Link.of(UriComponentsBuilder.fromUri(link.toUri())
                .queryParams(retrieveQueryParameters(pageFilter, giftCertificateSearchFilter))
                .build(true)
                .toString());
    }

    private static MultiValueMap<String, String> retrieveQueryParameters(PageFilter pageFilter,
                                                                  GiftCertificateSearchFilter giftCertificateSearchFilter) {
        MultiValueMap<String, String> resultMap = new LinkedMultiValueMap<>();

        if (giftCertificateSearchFilter.getGiftCertificateName() != null) {
            resultMap.add(GIFT_CERTIFICATE_NAME,
                    giftCertificateSearchFilter.getGiftCertificateName());
        }
        if (giftCertificateSearchFilter.getGiftCertificateDescription() != null) {
            resultMap.add(GIFT_CERTIFICATE_DESCRIPTION,
                    giftCertificateSearchFilter.getGiftCertificateDescription());
        }
        if (giftCertificateSearchFilter.getGiftCertificatesByDateOrder() != QueryOrder.NO) {
            resultMap.add(GIFT_CERTIFICATES_BY_DATE_ORDER,
                    giftCertificateSearchFilter.getGiftCertificatesByDateOrder().toString());
        }
        if (giftCertificateSearchFilter.getGiftCertificatesByNameOrder() != QueryOrder.NO) {
            resultMap.add(GIFT_CERTIFICATES_BY_NAME_ORDER,
                    giftCertificateSearchFilter.getGiftCertificatesByNameOrder().toString());
        }
        if (giftCertificateSearchFilter.getTags().size() != 0) {
            resultMap.put(TAGS, giftCertificateSearchFilter.getTags());
        }

        resultMap.addAll(PaginationEntityLinkHelper.getPagingParameters(pageFilter));

        return resultMap;
    }
}