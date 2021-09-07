package com.example.orderplanning.service;

import com.example.orderplanning.dao.CustomerWarehouseDistanceRepository;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Warehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerWarehouseDistanceService {
    private final CustomerWarehouseDistanceRepository repository;

    public void saveOrUpdate(CustomerWarehouseDistance entity) {
        repository.save(entity);
    }

    public List<CustomerWarehouseDistance> findByCustomer(Customer customer) {
        return repository.findByCustomer(customer);
    }

    public List<CustomerWarehouseDistance> findByCustomerSorted(Customer customer) {
        return repository.findByCustomerSorted(customer);
    }

    public Optional<CustomerWarehouseDistance> findByCustomerAndWarehouse(Customer customer, Warehouse warehouse) {
        return repository.findByCustomerAndWarehouse(customer, warehouse);
    }
}
