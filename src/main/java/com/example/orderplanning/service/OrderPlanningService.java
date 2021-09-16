package com.example.orderplanning.service;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.NoCustomerWithSuchIdException;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlanningService {
    private static final int PAGE_SIZE = 10;
    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final CustomerWarehouseDistanceService service;

    public void calculateDistanceToAllWarehouses(Customer customer) {
        List<CustomerWarehouseDistance> entities = service.findByCustomer(customer);
        if (entities.isEmpty()) {
            int i = 0;
            while (true) {
                Page<Warehouse> page = warehouseService.findAll(PageRequest.of(i++, PAGE_SIZE));
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
                Warehouse warehouse = warehouseService.findById(entity.getWarehouse().getId()).get();
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
                Page<Customer> page = customerService.findAll(PageRequest.of(i++, PAGE_SIZE));
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
                Customer customer = customerService.findById(entity.getCustomer().getId()).get();
                entity.setDistance(distance(customer, warehouse));
                service.saveOrUpdate(entity);
            });
        }
    }

    public void findNearestWarehouse(Order order) {
        Customer customer = customerService.findById(order.getCustomerId())
                .orElseThrow(() -> new NoCustomerWithSuchIdException("No customer with id " + order.getCustomerId()));
        Optional<CustomerWarehouseDistance> entities =
                service.findByCustomerAndProductName(customer, order.getProductName());
        if (entities.isEmpty()) {
            String message = "Can't find warehouses containing product " + order.getProductName();
            log.error(message);
            throw new NoWarehouseWithSuchProductException(message);
        }
        CustomerWarehouseDistance entity = entities.get();
        order.setWarehouse(entity.getWarehouse());
        order.setDistance(entity.getDistance());
    }

    private double distance(Customer customer, Warehouse warehouse) {
        return Math.sqrt(Math.pow(customer.getX() - warehouse.getX(), 2)
                + Math.pow(customer.getY() - warehouse.getY(), 2));
    }
}
