package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
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

    public void saveOrUpdate(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Warehouse findById(String id) {
        return warehouseRepository.findById(id).orElseThrow(NoWarehouseWithSuchIdException::new);
    }

    public void deleteById(String id) {
        warehouseRepository.deleteById(id);
    }
}
