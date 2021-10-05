package com.epam.esm.web.controller;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PageFilter;
import com.epam.esm.web.util.pagination.PaginationEntityLinkHelper;
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
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final String ORDERS = "orders";
    private static final String USERS = "users";
    private static final String USER = "user";
    private final UserService userService;
    private final ControllerHelper<UserDto> controllerHelper;

    @Autowired
    public UserController(UserService userService,
                          ControllerHelper<UserDto> controllerHelper) {
        this.userService = userService;
        this.controllerHelper = controllerHelper;
    }

    /**
     * Extracts all users from bd.
     * @param pageFilter object which contains information about page's number
     *                         and number of items for paging.
     * @param bindingResult need for catch problems with validating.
     * @return list of users on JSON format.
     */
    @GetMapping()
    public CollectionModel<EntityModel<UserDto>> retrieveAllUsers(@Valid PageFilter pageFilter,
                                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        List<UserDto> users = userService.findAllUsers(pageFilter);
        List<EntityModel<UserDto>> model = users.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(user -> user.add(getUserByIdLink(user.getContent().getId())
                .withRel(USER)));

        CollectionModel<EntityModel<UserDto>> result = CollectionModel.of(model);

        Link selfLink = getAllUsersLink(pageFilter, bindingResult)
                .withSelfRel();
        result.add(selfLink);

        controllerHelper.retrievePaginationLinks(pageFilter, selfLink, result);

        return result;
    }

    /**
     * Extracts user by id from bd.
     * @param id user's id.
     * @return found user on JSON format.
     */
    @GetMapping("/{id}")
    public EntityModel<UserDto> retrieveUserById(@PathVariable("id") Long id) {
        UserDto userDto = userService.findUserById(id);
        EntityModel<UserDto> model = EntityModel.of(userDto);

        model.add(getUserByIdLink(id).withSelfRel());

        model.add(getAllUsersLink(PageFilter.builder().build(), null)
                .withRel(USERS));

        model.add(OrderController.getAllUserOrdersLink(PageFilter.builder().build(), null, id)
                .withRel(ORDERS));

        return model;
    }

    static Link getUserByIdLink(Long userId) {
        return Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveUserById(userId))
                .toString());
    }

    static Link getAllUsersLink(PageFilter pageFilter,
                                 BindingResult bindingResult) {

        Link link = Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UserController.class)
                        .retrieveAllUsers(pageFilter, bindingResult))
                .toString());

        return Link.of(UriComponentsBuilder.fromUri(link.toUri())
                .queryParams(PaginationEntityLinkHelper.getPagingParameters(pageFilter))
                .build(true)
                .toString());
    }
}
