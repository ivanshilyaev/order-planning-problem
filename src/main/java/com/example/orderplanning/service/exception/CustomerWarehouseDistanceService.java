package com.example.orderplanning.service.exception;

import com.example.orderplanning.dao.CustomerWarehouseDistanceRepository;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Warehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return repository.findByCustomerId(customer);
    }

    public List<CustomerWarehouseDistance> findByWarehouse(Warehouse warehouse) {
        return repository.findByWarehouseId(warehouse);
    }

    public Page<CustomerWarehouseDistance> findByCustomerAndProductName(Customer customer, String productName,
                                                                        Pageable pageable) {
        return repository.findByCustomerIdAndProductName(customer, productName, pageable);
    }

    public Optional<CustomerWarehouseDistance> findByCustomerAndWarehouse(Customer customer, Warehouse warehouse) {
        return repository.findByCustomerIdAndWarehouseId(customer, warehouse);
    }

    public void deleteByCustomer(Customer customer) {
        repository.deleteByCustomer(customer);
    }

    public void deleteByWarehouse(Warehouse warehouse) {
        repository.deleteByWarehouse(warehouse);
    }
}
