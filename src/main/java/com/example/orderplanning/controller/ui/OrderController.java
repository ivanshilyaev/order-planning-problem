package com.example.orderplanning.controller.ui;

import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.OrderResponse;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final OrderPlanningService mainService;

    @Autowired
    public OrderController(OrderService orderService, OrderPlanningService mainService) {
        this.orderService = orderService;
        this.mainService = mainService;
    }

    // returns the nearest to the customer warehouse,
    // containing the product, and distance to that warehouse
    @GetMapping("/createOrder")
    public String createOrderForm(Model model) {
        model.addAttribute("order", new Order());
        return "order/createOrder";
    }

    @PostMapping("/createOrder")
    public String createOrderSubmit(@ModelAttribute Order order, Model model) {
        orderService.saveOrUpdate(order);
        OrderResponse orderResponse = mainService.findNearestWarehouse(order);
        model.addAttribute("orderResponse", orderResponse);
        return "order/orderResponse";
    }

    @GetMapping("/allOrders")
    public String listAllOrders(Model model) {
        model.addAttribute("orders", orderService.findAll());
        return "order/allOrders";
    }
}
