package com.example.orderplanning.service;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.OrderResponse;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderPlanningService {
    private final Logger logger = LoggerFactory.getLogger("Logger");
    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final ProductService productService;

    @Autowired
    public OrderPlanningService(WarehouseService warehouseService,
                                CustomerService customerService,
                                ProductService productService
    ) {
        this.warehouseService = warehouseService;
        this.customerService = customerService;
        this.productService = productService;
    }

    public OrderResponse findNearestWarehouse(Order order) {
        List<Warehouse> warehouses = productService
                .findAll()
                .stream()
                .filter(p -> p.getName().equals(order.getProductName()))
                .map(p -> warehouseService.findById(p.getWarehouseId()))
                .collect(Collectors.toList());
        if (warehouses.isEmpty()) {
            String message = "Can't find warehouses containing product " + order.getProductName();
            logger.error(message);
            throw new NoWarehouseWithSuchProductException(message);
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
        return new OrderResponse(closestWarehouse, minDistance);
    }

    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
