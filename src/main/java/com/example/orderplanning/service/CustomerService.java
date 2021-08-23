package com.example.orderplanning.service;

import com.example.orderplanning.dao.CustomerRepository;
import com.example.orderplanning.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public void deleteById(String id) {
        customerRepository.deleteById(id);
    }
}
