package com.example.orderplanning.service;

import com.example.orderplanning.entity.*;
import com.example.orderplanning.service.exception.NoCustomerWithSuchIdException;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchProductException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderPlanningService {
    private final WarehouseService warehouseService;
    private final CustomerService customerService;
    private final CustomerWarehouseDistanceService service;

    public void calculateDistanceToAllWarehouses(Customer customer) {
        List<CustomerWarehouseDistance> entities = service.findByCustomer(customer);
        if (entities.isEmpty()) {
            warehouseService.findAll(Pageable.unpaged())
                    .forEach(warehouse -> {
                        CustomerWarehouseDistance entity = CustomerWarehouseDistance.builder()
                                .customer(customer)
                                .warehouse(warehouse)
                                .distance(distance(customer, warehouse))
                                .build();
                        service.saveOrUpdate(entity);
                    });
        } else {
            entities.forEach(entity -> {
                entity.setCustomer(customer);
                Warehouse warehouse = warehouseService.findById(entity.getWarehouse().getId()).get();
                entity.setDistance(distance(customer, warehouse));
                service.saveOrUpdate(entity);
            });
        }
    }

    public void findNearestWarehouse(Order order) {
        Customer customer = customerService.findById(order.getCustomerId())
                .orElseThrow(() -> new NoCustomerWithSuchIdException("No customer with id " + order.getCustomerId()));
        List<CustomerWarehouseDistance> entities =
                service.findByCustomerAndProductName(customer, order.getProductName());
        if (entities.isEmpty()) {
            String message = "Can't find warehouses containing product " + order.getProductName();
            log.error(message);
            throw new NoWarehouseWithSuchProductException(message);
        }
        CustomerWarehouseDistance entity = entities.get(0);
        order.setWarehouse(warehouseService.findById(entity.getWarehouse().getId()).get());
        order.setDistance(entity.getDistance());
    }

    private double distance(Customer customer, Warehouse warehouse) {
        return Math.sqrt(Math.pow(customer.getX() - warehouse.getX(), 2)
                + Math.pow(customer.getY() - warehouse.getY(), 2));
    }
}
