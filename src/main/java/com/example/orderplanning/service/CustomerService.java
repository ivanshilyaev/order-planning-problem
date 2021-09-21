package com.example.orderplanning.service;

import com.example.orderplanning.dao.CustomerRepository;
import com.example.orderplanning.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final OrderPlanningService orderPlanningService;

    @Transactional
    public void save(Customer customer) {
        customerRepository.save(customer);
        orderPlanningService.calculateDistanceToAllWarehouses(customer);
    }

    @Transactional
    public Customer update(Customer newCustomer, Long id) {
        return findById(id).map(customer -> {
            int previousX = customer.getX();
            int previousY = customer.getY();
            customer.setName(newCustomer.getName());
            customer.setX(newCustomer.getX());
            customer.setY(newCustomer.getY());
            save(customer);
            if (previousX != customer.getX() || previousY != customer.getY()) {
                orderPlanningService.calculateDistanceToAllWarehouses(customer);
            }
            return customer;
        }).orElseGet(() -> {
            newCustomer.setId(id);
            save(newCustomer);
            orderPlanningService.calculateDistanceToAllWarehouses(newCustomer);
            return newCustomer;
        });
    }

    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
