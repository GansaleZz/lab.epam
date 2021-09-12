package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.ArrayDeque;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<?>> listOfUsers() {
        ResponseEntity<List<UserDto>> responseEntity =
                new ResponseEntity<>(userService.findAllUsers(),
                        HttpStatus.OK);
        List<UserDto> list = responseEntity.getBody();
        List<EntityModel<UserDto>> model = list.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(
                gift -> gift.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .userById(gift.getContent().getId()))
                        .withRel("href"))
        );

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> userById(@PathVariable("id") Long id) {
        ResponseEntity<UserDto> responseEntity =
                new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
        UserDto userDto = responseEntity.getBody();

        EntityModel<UserDto> model = EntityModel.of(userDto);
        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                                .methodOn(OrderController.class).listOfUserOrders(id))
                .withRel("orders"));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
