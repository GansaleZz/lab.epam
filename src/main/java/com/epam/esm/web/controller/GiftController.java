package com.epam.esm.web.controller;

import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.gift.GiftService;
import com.epam.esm.util.validation.BaseGiftValidator;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gifts")
public class GiftController {
    private final GiftService giftService;
    private final BaseGiftValidator<GiftCertificateDto, Long> giftValidation;

    @Autowired
    public GiftController(GiftService giftService, BaseGiftValidator<GiftCertificateDto, Long> giftValidation) {
        this.giftService = giftService;
        this.giftValidation = giftValidation;
    }

    /**
     * Extracts all gift certificates from bd.
     * @param giftSearchFilter need to set settings of searching gift certificates on bd.
     * @return found list of gift certificates on JSON format.
     * @throws EntityBadInputException if one of filters fields is incorrect.
     */
    @GetMapping
    public ResponseEntity<List<GiftCertificateDto>> listOfGifts(@RequestBody(required = false) GiftSearchFilter giftSearchFilter) throws EntityBadInputException {
        if (giftSearchFilter == null) {
            giftSearchFilter = new GiftSearchFilter();
        }

        giftValidation.onBeforeFindAllEntities(giftSearchFilter);
        return new ResponseEntity<>(giftService.findAllGifts(giftSearchFilter), HttpStatus.OK);
    }

    /**
     * Extracts gift certificate by id from bd.
     * @param id of gift certificate.
     * @return gift certificate (if exists) on JSON format.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<GiftCertificateDto> giftById(@PathVariable("id") Long id) throws EntityNotFoundException {
        GiftCertificateDto giftCertificateDto = giftService.findGiftById(id);
        return new ResponseEntity<>(giftCertificateDto, HttpStatus.OK);
    }

    /**
     * Creating gift certificate on bd.
     * @param dto gift certificate, which we want create on bd.
     * @return created gift certificate on JSON format.
     * @throws EntityBadInputException if one of params is incorrect.
     */
    @PostMapping
    public ResponseEntity<GiftCertificateDto> create(@RequestBody GiftCertificateDto dto) throws EntityBadInputException {
        giftValidation.onBeforeInsert(dto);
        return new ResponseEntity<>(giftService.create(dto), HttpStatus.CREATED);
    }

    /**
     * Updating gift certificate on bd.
     * @param id of gift certificate.
     * @param dto params of gift certificate which we want to update.
     * @return updated gift certificate on JSON format.
     * @throws EntityNotFoundException if gift certificate does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GiftCertificateDto> update(@PathVariable("id") Long id,
                                                     @RequestBody GiftCertificateDto dto ) throws EntityNotFoundException {
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
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) throws EntityNotFoundException {
        return new ResponseEntity<>(giftService.delete(id), HttpStatus.OK);
    }
}