package com.epam.esm.web.controller;

import com.epam.esm.service.dto.GiftCertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users/{userId}/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

//    @GetMapping()
//    public ResponseEntity<List<OrderDto>> listOfOrders(){
//        return new ResponseEntity<>(orderService.findAllOrders(), HttpStatus.OK);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderDto> orderById(@PathVariable("id") Long id)
//            throws EntityNotFoundException {
//        return new ResponseEntity<>(orderService.findOrderById(id), HttpStatus.OK);
//    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> listOfUserOrders(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(orderService.findOrdersByUserId(userId), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<OrderDto> create(@RequestBody Map<String, Long> id) {
//        return new ResponseEntity<>(orderService.create(id.get("giftId"), id.get("userId")), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id)
//            throws EntityNotFoundException {
//        return new ResponseEntity<>(orderService.delete(id), HttpStatus.OK);
//    }
}
