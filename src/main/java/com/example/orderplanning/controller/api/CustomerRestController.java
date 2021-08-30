package com.example.orderplanning.controller.api;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/api/addCustomer")
    public Customer addCustomer(@Valid @RequestBody Customer customer) {
        customerService.saveOrUpdate(customer);
        return customer;
    }

    @GetMapping("/api/allCustomers")
    public List<Customer> listAllCustomers() {
        return customerService.findAll();
    }

    @PostMapping("/api/deleteCustomerById")
    public void deleteCustomerById(@RequestParam String id) {
        customerService.deleteById(id);
    }
}
