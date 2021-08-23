package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void save(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public void deleteById(String id) {
        warehouseRepository.deleteById(id);
    }
}
