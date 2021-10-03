package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.util.exception.EntityBadInputException;
import com.epam.esm.web.util.pagination.PageFilter;
import com.epam.esm.web.util.pagination.PaginationEntityLinkHelper;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {

    private static final String ORDER = "order";
    private static final String ORDERS = "orders";
    private static final String USER = "user";
    private final OrderService orderService;
    private final ControllerHelper<OrderDto> controllerHelper;

    @Autowired
    public OrderController(OrderService orderService,
                           ControllerHelper<OrderDto> controllerHelper) {
        this.orderService = orderService;
        this.controllerHelper = controllerHelper;
    }

    /**
     * Extracts all user's orders from bd.
     * @param pageFilter object which contains information about page's number
     *                   and number of items for paging.
     * @param userId user's id.
     * @param bindingResult need for catch problems with validating.
     * @return found list of user's orders on JSON format.
     */
    @GetMapping("/users/{userId}/orders")
    public CollectionModel<EntityModel<OrderDto>> retrieveAllUserOrders(@Valid PageFilter pageFilter,
                                                                        BindingResult bindingResult,
                                                                        @PathVariable("userId") Long userId) {
        if (bindingResult.hasErrors()) {
            throw new EntityBadInputException(bindingResult.getFieldError().getField());
        }

        List<OrderDto> orders = orderService.findAllOrdersByUserId(pageFilter, userId);
        List<EntityModel<OrderDto>> model = orders.stream()
                .map(EntityModel::of).collect(Collectors.toList());

        model.forEach(order -> order.add(getOrderByIdLink(order.getContent().getId())
                .withRel(ORDER)));

        CollectionModel<EntityModel<OrderDto>> result = CollectionModel.of(model);

        Link selfLink = getAllUserOrdersLink(pageFilter, bindingResult, userId)
                .withSelfRel();

        result.add(selfLink);

        controllerHelper.retrievePaginationLinks(pageFilter, selfLink, result);

        return result;
    }

    /**
     * Extracts order by id from bd.
     * @param id order's id.
     * @return order (if exists) on JSON format.
     */
    @GetMapping("orders/{id}")
    public EntityModel<OrderDto> retrieveOrderById(@PathVariable("id") Long id) {
        EntityModel<OrderDto> model = EntityModel.of(orderService.findOrderById(id));

        model.add(getOrderByIdLink(id).withSelfRel());

        model.add(getAllUserOrdersLink(PageFilter.builder().build(), null, id)
                .withRel(ORDERS));
        model.add(UserController.getUserByIdLink(model.getContent().getUser().getUserId())
                .withRel(USER));

        return model;
    }

    /**
     * Creating order on bd.
     * @param userId user's id.
     * @param giftCertificateDto gift certificate, which user buying.
     * @return created order.
     */
    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<OrderDto> createOrder(@PathVariable("userId") Long userId,
                                                @RequestBody GiftCertificateDto giftCertificateDto) {
        return new ResponseEntity<>(orderService.createOrder(userId,
                giftCertificateDto.getId()),
                HttpStatus.OK);
    }

    static Link getAllUserOrdersLink(PageFilter pageFilter,
                                     BindingResult bindingResult,
                                     Long userId) {

        Link link = Link.of(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveAllUserOrders(pageFilter, bindingResult, userId))
                .toString());

        return Link.of(UriComponentsBuilder.fromUri(link.toUri())
                .queryParams(PaginationEntityLinkHelper.getPagingParameters(pageFilter))
                .build(true)
                .toString());
    }

    static Link getOrderByIdLink(Long orderId) {
        return Link.of(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class)
                        .retrieveOrderById(orderId))
                .toString());
    }
}
