package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private static final String USER = "user";
    private static final String ORDER = "order";
    private static final String ORDERS = "orders";

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<?> orderById(@PathVariable("userId") Long userId,
                                              @PathVariable("id") Long id)
            throws EntityNotFoundException {
        ResponseEntity<OrderDto> responseEntity =
                new ResponseEntity<>(orderService.findOrderById(id), HttpStatus.OK);
        EntityModel<OrderDto> model = EntityModel.of(responseEntity.getBody());

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(this.getClass()).orderById(userId, id)).withSelfRel());
        Link link = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(OrderController.class).listOfUserOrders(userId))
                .withRel(ORDERS);
        model.add(link);

        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> listOfUserOrders(@PathVariable("userId") Long userId) {
        ResponseEntity<List<OrderDto>> responseEntity =
                new ResponseEntity<>(orderService.findOrdersByUserId(userId),
                        HttpStatus.OK);
        List<OrderDto> list = responseEntity.getBody();
        List<EntityModel<OrderDto>> model = list.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(
                order -> order.add(WebMvcLinkBuilder
                        .linkTo(WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .orderById(userId, order.getContent().getId()))
                        .withRel(ORDER))
        );

        CollectionModel<EntityModel<OrderDto>> result = CollectionModel.of(model);
        result.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(this.getClass()).listOfUserOrders(userId))
                .withSelfRel());
        Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(UserController.class).userById(userId)).withRel(USER);
        result.add(link);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@PathVariable("userId") Long userId,
                                           @RequestBody GiftCertificateDto giftCertificateDto) {
        return new ResponseEntity<>(orderService.create(userId,
                giftCertificateDto.getId()), HttpStatus.OK);
    }
}
