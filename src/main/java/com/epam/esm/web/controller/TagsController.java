package com.epam.esm.web.controller;

import com.epam.esm.model.domain.Tag;
import com.epam.esm.model.dto.TagDto;
import com.epam.esm.model.util.mapper.TagMapperImpl;
import com.epam.esm.persistence.jdbc.JdbcTemplateTagDaoImpl;
import com.epam.esm.web.exception.TagBadInputException;
import com.epam.esm.web.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagsController{
    private final JdbcTemplateTagDaoImpl jdbcTemplateTagDao;

    @Autowired
    public TagsController(JdbcTemplateTagDaoImpl jdbcTemplateTagDao){
        this.jdbcTemplateTagDao = jdbcTemplateTagDao;
    }

    @GetMapping()
    public ResponseEntity<List<Tag>> listOfTags(){
        List<Tag> list = new ArrayList<>();
        jdbcTemplateTagDao.findAllEntities().forEach(i -> list.add(new TagMapperImpl().toEntity(i)));
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Tag> tagById(@PathVariable("id") int id) throws TagNotFoundException {
        Optional<TagDto> tag = jdbcTemplateTagDao.findEntityById(id);
        if(tag.isPresent()){
            return new ResponseEntity<>(new TagMapperImpl().toEntity(tag.get()), HttpStatus.OK);
        }else{
            throw new TagNotFoundException("Requested tag not found (id = "+id+")");
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Tag> tagByName(@PathVariable("name") String name) throws TagNotFoundException{
        Optional<TagDto> tag = jdbcTemplateTagDao.findTagByName(name);
        if(tag.isPresent()){
            return new ResponseEntity<>(new TagMapperImpl().toEntity(tag.get()), HttpStatus.OK);
        }else{
            throw new TagNotFoundException("Requested tag not found (name = "+name+")");
        }
    }

    @PostMapping()
    public ResponseEntity<Boolean> create(@RequestBody Tag tag) throws TagBadInputException{
        if(tag.getName() == null){
            throw new TagBadInputException("Please enter all the details");
        }
        return new ResponseEntity<>(jdbcTemplateTagDao.create(new TagMapperImpl().toDto(tag)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") int id) throws TagBadInputException{
        if(jdbcTemplateTagDao.delete(id)){
            return new ResponseEntity<>(true, HttpStatus.OK);
        }else{
            throw new TagBadInputException("Requested tag not found (id = "+id+")");
        }
    }
}
