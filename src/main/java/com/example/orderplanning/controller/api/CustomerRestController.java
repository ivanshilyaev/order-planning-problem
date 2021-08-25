package com.example.orderplanning.controller.api;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerRestController {
    private final CustomerService customerService;

    @Autowired
    public CustomerRestController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/api/addCustomer")
    public Customer addCustomer(@RequestParam String id,
                                @RequestParam int x,
                                @RequestParam int y
    ) {
        Customer customer = new Customer(id, x, y);
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
