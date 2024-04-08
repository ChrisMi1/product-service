package com.programmingmicroservices.orderservice.controller;

import com.programmingmicroservices.orderservice.dto.OrderRequest;
import com.programmingmicroservices.orderservice.service.OrderService;
import jakarta.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest)throws IllegalArgumentException{
        orderService.placeOrder(orderRequest);
        return "Order placed successfully";
    }
}
