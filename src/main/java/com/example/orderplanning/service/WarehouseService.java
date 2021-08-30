package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {
    private static final Logger logger = LoggerFactory.getLogger(WarehouseService.class);
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
        Optional<Warehouse> warehouseOptional = warehouseRepository.findById(id);
        if (warehouseOptional.isPresent()) {
            return warehouseOptional.get();
        } else {
            logger.error("No warehouse with id " + id);
            throw new NoWarehouseWithSuchIdException();
        }
    }

    public void deleteById(String id) {
        warehouseRepository.deleteById(id);
    }
}
