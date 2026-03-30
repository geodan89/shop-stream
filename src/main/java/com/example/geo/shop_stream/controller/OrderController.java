package com.example.geo.shop_stream.controller;

import com.example.geo.shop_stream.dto.CreateOrderRequest;
import com.example.geo.shop_stream.model.Order;
import com.example.geo.shop_stream.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Order placeOrder(@RequestBody @Valid CreateOrderRequest request) {
        return orderService.placeOrder(request);
    }
}
