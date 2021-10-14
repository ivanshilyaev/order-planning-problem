package com.example.orderplanning.service;

import com.example.orderplanning.dao.CustomerRepository;
import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.CustomerWarehouseDistanceService;
import com.example.orderplanning.service.exception.NoCustomerWithSuchIdException;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlanningService {
    private static final int PAGE_SIZE = 10;
    private final WarehouseRepository warehouseRepository;
    private final CustomerRepository customerRepository;
    private final CustomerWarehouseDistanceService service;

    public void calculateDistanceToAllWarehouses(Customer customer) {
        List<CustomerWarehouseDistance> entities = service.findByCustomer(customer);
        if (entities.isEmpty()) {
            int i = 0;
            while (true) {
                Page<Warehouse> page = warehouseRepository.findAll(PageRequest.of(i++, PAGE_SIZE));
                if (page.isEmpty()) {
                    break;
                }
                page.forEach(warehouse -> {
                    CustomerWarehouseDistance entity = CustomerWarehouseDistance.builder()
                            .customer(customer)
                            .warehouse(warehouse)
                            .distance(distance(customer, warehouse))
                            .build();
                    service.saveOrUpdate(entity);
                });
            }
        } else {
            entities.forEach(entity -> {
                entity.setCustomer(customer);
                Warehouse warehouse = warehouseRepository.findById(entity.getWarehouse().getId()).get();
                entity.setDistance(distance(customer, warehouse));
                service.saveOrUpdate(entity);
            });
        }
    }

    public void calculateDistanceToAllCustomers(Warehouse warehouse) {
        List<CustomerWarehouseDistance> entities = service.findByWarehouse(warehouse);
        if (entities.isEmpty()) {
            int i = 0;
            while (true) {
                Page<Customer> page = customerRepository.findAll(PageRequest.of(i++, PAGE_SIZE));
                if (page.isEmpty()) {
                    break;
                }
                page.forEach(customer -> {
                    CustomerWarehouseDistance entity = CustomerWarehouseDistance.builder()
                            .customer(customer)
                            .warehouse(warehouse)
                            .distance(distance(customer, warehouse))
                            .build();
                    service.saveOrUpdate(entity);
                });
            }
        } else {
            entities.forEach(entity -> {
                entity.setWarehouse(warehouse);
                Customer customer = customerRepository.findById(entity.getCustomer().getId()).get();
                entity.setDistance(distance(customer, warehouse));
                service.saveOrUpdate(entity);
            });
        }
    }

    public void findNearestWarehouse(Order order) {
        Customer customer = customerRepository.findById(order.getCustomerId())
                .orElseThrow(() -> new NoCustomerWithSuchIdException("No customer with id " + order.getCustomerId()));
        Page<CustomerWarehouseDistance> entities =
                service.findByCustomerAndProductName(customer, order.getProductName(), PageRequest.of(0, 1));
        CustomerWarehouseDistance entity = entities.stream().findFirst().orElseThrow(() -> {
            String message = "Can't find warehouses containing product " + order.getProductName();
            log.error(message);
            throw new NoWarehouseWithSuchProductException(message);
        });
        order.setWarehouse(entity.getWarehouse());
        order.setDistance(entity.getDistance());
    }

    private double distance(Customer customer, Warehouse warehouse) {
        return Math.sqrt(Math.pow(customer.getX() - warehouse.getX(), 2)
                + Math.pow(customer.getY() - warehouse.getY(), 2));
    }
}
