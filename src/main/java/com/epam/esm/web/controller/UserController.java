package com.epam.esm.web.controller;

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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String ORDERS = "orders";
    private static final String USERS = "users";
    private static final String USER = "user";

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<?> listOfUsers() {
        ResponseEntity<List<UserDto>> responseEntity =
                new ResponseEntity<>(userService.findAllUsers(),
                        HttpStatus.OK);
        List<UserDto> list = responseEntity.getBody();
        List<EntityModel<UserDto>> model = list.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(
                user -> user.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .userById(user.getContent().getId()))
                        .withRel(USER))
        );
        CollectionModel<EntityModel<UserDto>> result = CollectionModel.of(model);
        result.add(WebMvcLinkBuilder.linkTo(this.getClass())
                .withSelfRel());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> userById(@PathVariable("id") Long id) {
        ResponseEntity<UserDto> responseEntity =
                new ResponseEntity<>(userService.findUserById(id), HttpStatus.OK);
        UserDto userDto = responseEntity.getBody();
        EntityModel<UserDto> model = EntityModel.of(userDto);

        Link selfRel = WebMvcLinkBuilder.linkTo(this.getClass())
                .slash(id)
                .withRel(USERS);

        Link usersRel = WebMvcLinkBuilder.linkTo(this.getClass())
                .withSelfRel();

        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                                .methodOn(OrderController.class).listOfUserOrders(id))
                .withRel(ORDERS), selfRel, usersRel);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }
}
