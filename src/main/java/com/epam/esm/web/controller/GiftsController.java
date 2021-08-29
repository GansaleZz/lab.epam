package com.epam.esm.web.controller;

import com.epam.esm.model.domain.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.util.mapper.GiftCertificateMapperImpl;
import com.epam.esm.persistence.jdbc.JdbcTemplateGiftDaoImpl;
import com.epam.esm.web.exception.GiftBadInputException;
import com.epam.esm.web.exception.GiftNotFoundException;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gifts")
public class GiftsController {
    private final JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao;

    @Autowired
    public GiftsController(JdbcTemplateGiftDaoImpl jdbcTemplateGiftDao){
        this.jdbcTemplateGiftDao = jdbcTemplateGiftDao;
    }

    @GetMapping()
    public ResponseEntity<List<GiftCertificate>> listOfGifts(){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.findAllEntities().forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/id/{id}")
    public ResponseEntity<GiftCertificate> giftById(@PathVariable("id") Integer id) throws GiftNotFoundException{
        Optional<GiftCertificateDto> giftCertificateDto = jdbcTemplateGiftDao.findEntityById(id);
        if(giftCertificateDto.isPresent()){
            return new ResponseEntity<>(new GiftCertificateMapperImpl().toEntity(jdbcTemplateGiftDao.findEntityById(id).get()), HttpStatus.OK);
        }else{
            throw new GiftNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<List<GiftCertificate>> giftsByName(@PathVariable("name") String name){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.findGiftsByName(name).forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/description/{description}")
    public ResponseEntity<List<GiftCertificate>> giftsByDescription(@PathVariable("description") String description){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.findGiftsByDescription(description).forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/tag/{tagName}")
    public ResponseEntity<List<GiftCertificate>> giftsByTag(@PathVariable("tagName") String tagName){
        List<GiftCertificate> list = new ArrayList<>();
        TagDto tag = TagDto.builder().build();
        tag.setName(tagName);
        jdbcTemplateGiftDao.findGiftsByTag(tag).forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/sortByNameASC")
    public ResponseEntity<List<GiftCertificate>> sortByNameASC(){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.sortGiftsByNameASC().forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/sortByNameDESC")
    public ResponseEntity<List<GiftCertificate>> sortByNameDESC(){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.sortGiftsByNameDESC().forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/sortByDescriptionASC")
    public ResponseEntity<List<GiftCertificate>> sortByDescriptionASC(){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.sortGiftsByDescriptionASC().forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/sortByDescriptionDESC")
    public ResponseEntity<List<GiftCertificate>> sortByDescriptionDESC(){
        List<GiftCertificate> list = new ArrayList<>();
        jdbcTemplateGiftDao.sortGiftsByDescriptionDESC().forEach(i -> list.add(new GiftCertificateMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<GiftCertificate> create(@RequestBody GiftCertificate giftCertificate) throws GiftBadInputException{
        if(giftCertificate.getDescription() == null || giftCertificate.getDuration().equals(0) || giftCertificate.getName() == null || giftCertificate.getPrice().equals(0.0)){
            throw new GiftBadInputException("Please enter all the details");
        }
        giftCertificate.setCreateDate(Calendar.getInstance().getTime());
        giftCertificate.setLastUpdateDate(Calendar.getInstance().getTime());

        if(giftCertificate.getTagName() != null){
            TagDto tag = TagDto.builder().build();
            tag.setName(giftCertificate.getTagName());
            jdbcTemplateGiftDao.create(new GiftCertificateMapperImpl().toDto(giftCertificate), tag);
        }else{
            jdbcTemplateGiftDao.create(new GiftCertificateMapperImpl().toDto(giftCertificate));
        }
        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable("id") int id, @RequestBody GiftCertificate giftCertificate ) throws GiftNotFoundException{
        if(jdbcTemplateGiftDao.findEntityById(id).isPresent()) {
            GiftCertificateDto giftCertificateOld = jdbcTemplateGiftDao.findEntityById(id).get();
            if (giftCertificate.getName() != null) {
                giftCertificateOld.setName(giftCertificate.getName());
            }
            if (giftCertificate.getDescription() != null) {
                giftCertificateOld.setDescription(giftCertificate.getDescription());
            }
            if (giftCertificate.getPrice() != null) {
                giftCertificateOld.setPrice(giftCertificate.getPrice());
            }
            if (giftCertificate.getDuration() != null) {
                giftCertificateOld.setDuration(giftCertificate.getDuration());
            }
            giftCertificateOld.setLastUpdateDate(Calendar.getInstance().getTime());
            if (giftCertificate.getTagName() != null) {
                TagDto tag = TagDto.builder().build();
                tag.setName(giftCertificate.getTagName());
                return new ResponseEntity<>(jdbcTemplateGiftDao.update(giftCertificateOld, tag), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(jdbcTemplateGiftDao.update(giftCertificateOld), HttpStatus.OK);
            }
        }else{
            throw new GiftNotFoundException("Requested gift not found (id = "+id+")");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id) throws GiftNotFoundException{
        if(jdbcTemplateGiftDao.delete(id)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            throw new GiftNotFoundException("Requested gift not found (id = "+id+")");
        }
    }
}