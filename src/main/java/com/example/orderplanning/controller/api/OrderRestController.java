package com.example.orderplanning.controller.api;

import com.example.orderplanning.entity.*;
import com.example.orderplanning.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class OrderRestController {

    private final OrderService orderService;
    private final OrderPlanningService mainService;

    @Autowired
    public OrderRestController(OrderService orderService, OrderPlanningService mainService) {
        this.orderService = orderService;
        this.mainService = mainService;
    }

    // returns the nearest to the customer warehouse,
    // containing the product, and distance to that warehouse
    @PostMapping("/api/createOrder")
    public OrderResponse createOrder(@RequestParam String customerId,
                                     @RequestParam String productName
    ) {
        Order order = new Order(customerId, productName);
        orderService.saveOrUpdate(order);
        return mainService.findNearestWarehouse(order);
    }

    @GetMapping("/api/allOrders")
    public List<Order> listAllOrders() {
        return orderService.findAll();
    }
}
