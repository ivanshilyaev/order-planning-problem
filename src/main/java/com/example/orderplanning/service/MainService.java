package com.example.orderplanning.service;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MainService {
    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public MainService(WarehouseService warehouseService,
                       CustomerService customerService,
                       ProductService productService
    ) {
        this.warehouseService = warehouseService;
        this.customerService = customerService;
        this.productService = productService;
    }

    public Warehouse findNearestWarehouse(Order order) {
        List<Warehouse> warehouses = productService
                .findAll()
                .stream()
                .filter(p -> p.getName().equals(order.getProductName()))
                .map(p -> warehouseService.findById(p.getWarehouseId()))
                .collect(Collectors.toList());
        if (warehouses.isEmpty()) {
            throw new NoWarehouseWithSuchProductException("Can't find warehouses containing product "
                    + order.getProductName());
        }
        Customer customer = customerService.findById(order.getCustomerId());
        Warehouse closestWarehouse = warehouses.get(0);
        double minDistance = Double.MAX_VALUE;
        for (Warehouse warehouse : warehouses) {
            double currentDistance = distance(warehouse.getX(), warehouse.getY(), customer.getX(), customer.getY());
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
                closestWarehouse = warehouse;
            }
        }
        return closestWarehouse;
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
