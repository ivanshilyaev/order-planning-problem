package com.example.orderplanning.controller.ui;

import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WarehouseController {
    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/addWarehouse")
    public String addWarehouseForm(Model model) {
        model.addAttribute("warehouse", new Warehouse());
        return "warehouse/addWarehouse";
    }

    @PostMapping("/addWarehouse")
    public String addWarehouseSubmit(@ModelAttribute Warehouse warehouse) {
        warehouseService.saveOrUpdate(warehouse);
        return "redirect:/index";
    }

    @GetMapping("/allWarehouses")
    public String listAllWarehouses(Model model) {
        model.addAttribute("warehouses", warehouseService.findAll());
        return "warehouse/allWarehouses";
    }

    @GetMapping("/deleteWarehouseById")
    public String deleteWarehouseByIdForm() {
        return "warehouse/deleteWarehouseById";
    }

    @PostMapping("/deleteWarehouseById")
    public String deleteWarehouseByIdSubmit(@RequestParam(value = "id") String id) {
        warehouseService.deleteById(id);
        return "redirect:/index";
    }
}
