package com.example.orderplanning.controller;

import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping("/api/addWarehouse")
    public Warehouse addWarehouse(@Valid @RequestBody Warehouse warehouse) {
        warehouseService.saveOrUpdate(warehouse);
        return warehouse;
    }

    @GetMapping("/api/allWarehouses")
    public List<Warehouse> listAllWarehouses() {
        return warehouseService.findAll();
    }

    @PostMapping("/api/deleteWarehouseById")
    public void deleteWarehouseById(@RequestParam String id) {
        warehouseService.deleteById(id);
    }
}
