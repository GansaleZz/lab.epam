package com.epam.esm.web.controller;

import com.epam.esm.persistence.dao.OrderDao;
import com.epam.esm.persistence.util.search.GiftSearchFilter;
import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.gift.GiftService;
import com.epam.esm.web.exception.EntityBadInputException;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@RestController
@RequestMapping("/gifts")
public class GiftController {
    private final GiftService giftService;
    private final static String BAD_INPUT = "Please enter correct details for ";

    @Autowired
    public GiftController(GiftService giftService) {
        this.giftService = giftService;
    }

    /**
     * Extracts all gift certificates from bd.
     * @return found list of gift certificates on JSON format.
     * @throws EntityBadInputException if one of filters fields is incorrect.
     */
    @GetMapping()
    public ResponseEntity<List<GiftCertificateDto>> listOfGifts(@Valid GiftSearchFilter giftSearchFilter,
                                                                BindingResult bindingResult)
            throws EntityBadInputException {

        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(BAD_INPUT + bindingResult.getFieldError().getField());
        }

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
    public ResponseEntity<GiftCertificateDto> create(@Valid @RequestBody GiftCertificateDto dto, BindingResult error) {
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