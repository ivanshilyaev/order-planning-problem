package com.example.orderplanning.controller;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.Product;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.CustomerService;
import com.example.orderplanning.service.OrderService;
import com.example.orderplanning.service.ProductService;
import com.example.orderplanning.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderPlanningRestController {

    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final ProductService productService;

    @Autowired
    public OrderPlanningRestController(WarehouseService warehouseService,
                                       CustomerService customerService,
                                       OrderService orderService,
                                       ProductService productService
    ) {
        this.warehouseService = warehouseService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.productService = productService;
    }

    // customers' API

    @PostMapping("/api/addCustomer")
    public Customer addCustomer(@RequestBody Customer customer) {
        customerService.saveOrUpdate(customer);
        return customer;
    }

    @GetMapping("/api/allCustomers")
    public List<Customer> listAllCustomers() {
        return customerService.findAll();
    }

    // orders' API

    @PostMapping("/api/createOrder")
    public Order createOrder(@RequestBody Order order) {
        orderService.saveOrUpdate(order);
        return order;
    }

    // products' API

    @GetMapping("/api/allProducts")
    public List<String> listALlProducts() {
        return productService.findAll().stream().map(Product::getName).distinct().collect(Collectors.toList());
    }

    // warehouses' API

    @GetMapping("/api/allWarehouses")
    public List<Warehouse> listAllWarehouses() {
        return warehouseService.findAll();
    }

}
