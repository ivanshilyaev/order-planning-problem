package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.CustomerWarehouseDistanceService;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final OrderPlanningService orderPlanningService;
    private final CustomerWarehouseDistanceService cwdService;

    @Transactional
    public void save(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
        orderPlanningService.calculateDistanceToAllCustomers(warehouse);
    }

    @Transactional
    public Warehouse update(Warehouse newWarehouse, Long id) {
        return findById(id)
                .map(warehouse -> {
                    int previousX = warehouse.getX();
                    int previousY = warehouse.getY();
                    warehouse.setName(newWarehouse.getName());
                    warehouse.setX(newWarehouse.getX());
                    warehouse.setY(newWarehouse.getY());
                    save(warehouse);
                    if (previousX != warehouse.getX() || previousY != warehouse.getY()) {
                        orderPlanningService.calculateDistanceToAllCustomers(warehouse);
                    }
                    return warehouse;
                }).orElseGet(() -> {
                    newWarehouse.setId(id);
                    save(newWarehouse);
                    orderPlanningService.calculateDistanceToAllCustomers(newWarehouse);
                    return newWarehouse;
                });
    }

    public Page<Warehouse> findAll(Pageable pageable) {
        return warehouseRepository.findAll(pageable);
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        Warehouse warehouse = findById(id)
                .orElseThrow(() -> new NoWarehouseWithSuchIdException("No warehouse with id " + id));
        cwdService.deleteByWarehouse(warehouse);
        warehouseRepository.deleteById(id);
    }
}
