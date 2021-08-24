package com.example.orderplanning.service;

import com.example.orderplanning.dao.CustomerRepository;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.exception.NoCustomerWithSuchIdException;
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

    public void saveOrUpdate(Customer customer) {
        customerRepository.save(customer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Customer findById(String id) {
        return customerRepository.findById(id).orElseThrow(NoCustomerWithSuchIdException::new);
    }

    public void deleteById(String id) {
        customerRepository.deleteById(id);
    }
}
