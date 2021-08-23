package com.example.orderplanning.controller;

import com.example.orderplanning.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderPlanningController {

    private final WarehouseService warehouseService;

    @Autowired
    public OrderPlanningController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/")
    public String index() {
        return warehouseService.findAll().toString();
    }
}
