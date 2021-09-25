package com.epam.esm.web.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationEntityLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String NO_METHOD = "Method not found";
    private static final String BAD_INPUT = "Please enter correct details for ";
    private static final String LIST_OF_USERS = "listOfUsers";
    private static final String ORDERS = "orders";
    private static final String USERS = "users";
    private static final String USER = "user";
    private final UserService userService;
    private final PaginationEntityLink paginationUserLink;

    @Autowired
    public UserController(UserService userService,
                          PaginationEntityLink paginationEntityLink) {
        this.userService = userService;
        this.paginationUserLink = paginationEntityLink;
    }

    @GetMapping()
    public CollectionModel<EntityModel<UserDto>> listOfUsers(@Valid PaginationFilter paginationFilter,
                                                             BindingResult bindingResult)
            throws NoSuchMethodException {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(BAD_INPUT + bindingResult.getFieldError().getField());
        }

        Method listOfUsers = this.getClass().getMethod(LIST_OF_USERS,
                PaginationFilter.class,
                BindingResult.class);
        List<UserDto> users = userService.findAllUsers(paginationFilter);
        List<EntityModel<UserDto>> model = users.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(
                user -> {
                    try {
                        user.add(WebMvcLinkBuilder
                                .linkTo(WebMvcLinkBuilder
                                        .methodOn(this.getClass())
                                        .userById(user.getContent().getId()))
                                .withRel(USER));
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(NO_METHOD);
                    }
                }
        );

        CollectionModel<EntityModel<UserDto>> result = CollectionModel.of(model);

        result.add(WebMvcLinkBuilder.linkTo(listOfUsers,
                       paginationFilter,
                       bindingResult)
                .slash(String.format(PaginationEntityLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withSelfRel());
        if (paginationFilter.getItems() != 0) {
            paginationUserLink.nextLink(listOfUsers, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationUserLink.prevLink(listOfUsers, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationUserLink.firstLink(listOfUsers, paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationUserLink.lastLink(listOfUsers, paginationFilter, bindingResult)
                    .ifPresent(result::add);
        }
        return result;
    }

    @GetMapping("/{id}")
    public EntityModel<UserDto> userById(@PathVariable("id") Long id)
            throws NoSuchMethodException {
        UserDto userDto = userService.findUserById(id);
        EntityModel<UserDto> model = EntityModel.of(userDto);
        PaginationFilter paginationFilter = PaginationFilter.builder()
                .page(0)
                .items(1)
                .build();

        Method listOfUsers = this.getClass().getMethod(LIST_OF_USERS,
                PaginationFilter.class,
                BindingResult.class);

        Link selfRel = WebMvcLinkBuilder.linkTo(this.getClass())
                .slash(id)
                .withSelfRel();

        Link usersRel = WebMvcLinkBuilder.linkTo(listOfUsers,
                        paginationFilter,
                        null)
                .slash(String.format(PaginationEntityLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withRel(USERS);

        model.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                                .methodOn(OrderController.class)
                        .listOfUserOrders(paginationFilter, id, null))
                .slash(String.format(PaginationEntityLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withRel(ORDERS), selfRel, usersRel);

        return model;
    }
}
