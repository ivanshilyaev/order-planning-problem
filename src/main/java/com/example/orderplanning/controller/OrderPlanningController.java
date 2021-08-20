package com.example.orderplanning.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPlanningController {

    @GetMapping("/")
    public String index() {
        return "Order Planning Problem";
    }
}
