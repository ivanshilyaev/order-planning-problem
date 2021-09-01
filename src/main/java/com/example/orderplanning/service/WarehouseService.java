package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public void saveOrUpdate(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> findById(String id) {
        return warehouseRepository.findById(id);
    }

    public void deleteById(String id) {
        warehouseRepository.deleteById(id);
    }
}
