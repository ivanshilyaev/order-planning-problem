package com.example.orderplanning.controller;

import com.example.orderplanning.entity.*;
import com.example.orderplanning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderPlanningService orderPlanningService;

    @Autowired
    public OrderController(OrderService orderService, OrderPlanningService orderPlanningService) {
        this.orderService = orderService;
        this.orderPlanningService = orderPlanningService;
    }

    // returns the nearest to the customer warehouse,
    // containing the product, and distance to that warehouse
    @PostMapping("/api/createOrder")
    public OrderResponse createOrder(@Valid @RequestBody Order order) {
        orderService.saveOrUpdate(order);
        return orderPlanningService.findNearestWarehouse(order);
    }

    @GetMapping("/api/allOrders")
    public List<Order> listAllOrders() {
        return orderService.findAll();
    }
}
