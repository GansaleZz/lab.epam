package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PaginationFilter;
import com.epam.esm.web.util.pagination.link.PaginationOrderLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    private static final String NO_METHOD = "Method not found";
    private static final String ORDER = "order";
    private static final String ORDERS = "orders";
    private static final String LIST_OF_ORDERS = "listOfUserOrders";
    private final OrderService orderService;
    private final PaginationOrderLink paginationOrderLink;

    @Autowired
    public OrderController(OrderService orderService,
                           PaginationOrderLink paginationOrderLink) {
        this.orderService = orderService;
        this.paginationOrderLink = paginationOrderLink;
    }

    /**
     * Extracts all user's orders from bd.
     * @param paginationFilter - object which contains information about page's number
     *                         and number of items for paging.
     * @param userId - user's id.
     * @param bindingResult - need for catch problems with validating.
     * @return found list of user's orders on JSON format.
     */
    @GetMapping()
    public CollectionModel<EntityModel<OrderDto>> listOfUserOrders(@Valid PaginationFilter paginationFilter,
                                                                   @PathVariable("userId") Long userId,
                                                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        List<OrderDto> orders = orderService.findOrdersByUserId(paginationFilter, userId);
        List<EntityModel<OrderDto>> model = orders.stream()
                .map(EntityModel::of).collect(Collectors.toList());
        Method listOfOrders;
        try {
            listOfOrders = this.getClass().getMethod(LIST_OF_ORDERS,
                    PaginationFilter.class,
                    Long.class,
                    BindingResult.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(NO_METHOD);
        }

        model.forEach(order -> order.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(this.getClass())
                        .orderById(userId,
                                order.getContent().getId()))
                .withRel(ORDER)));

        CollectionModel<EntityModel<OrderDto>> result = CollectionModel.of(model);

        result.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                                .listOfUserOrders(paginationFilter,
                                        userId,
                                        bindingResult))
                .slash(String.format(PaginationOrderLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withSelfRel());

        if (paginationFilter.getItems() != 0) {
            paginationOrderLink.nextLink(listOfOrders, userId,
                            paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationOrderLink.prevLink(listOfOrders, userId,
                            paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationOrderLink.firstLink(listOfOrders, userId,
                            paginationFilter, bindingResult)
                    .ifPresent(result::add);
            paginationOrderLink.lastLink(listOfOrders, userId,
                            paginationFilter, bindingResult)
                    .ifPresent(result::add);
        }

        return result;
    }

    /**
     * Extracts order by id from bd.
     * @param userId - user's id.
     * @param id - order's id.
     * @return order (if exists) on JSON format.
     */
    @GetMapping("/{id}")
    public EntityModel<OrderDto> orderById(@PathVariable("userId") Long userId,
                                           @PathVariable("id") Long id) {
        EntityModel<OrderDto> model = EntityModel.of(orderService.findOrderById(id, userId));
        PaginationFilter paginationFilter = PaginationFilter.builder().build();

        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder
                .methodOn(this.getClass())
                .orderById(userId, id))
                .withSelfRel());

        Link orders = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass())
                        .listOfUserOrders(paginationFilter,
                                userId,
                                null))
                .slash(String.format(PaginationOrderLink.PAGE_ITEMS,
                        paginationFilter.getPage(),
                        paginationFilter.getItems()))
                .withRel(ORDERS);
        model.add(orders);

        return model;
    }

    /**
     * Creating order on bd.
     * @param userId - user's id.
     * @param giftCertificateDto - gift certificate, which user buying.
     * @return created order.
     */
    @PostMapping
    public ResponseEntity<OrderDto> create(@PathVariable("userId") Long userId,
                                           @RequestBody GiftCertificateDto giftCertificateDto) {
        return new ResponseEntity<>(orderService.create(userId,
                giftCertificateDto.getId()),
                HttpStatus.OK);
    }
}
