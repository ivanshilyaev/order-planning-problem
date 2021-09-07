package com.example.orderplanning.service;

import com.example.orderplanning.dao.WarehouseRepository;
import com.example.orderplanning.entity.Warehouse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public void saveOrUpdate(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    public Optional<Warehouse> findById(Long id) {
        return warehouseRepository.findById(id);
    }

    public void deleteById(Long id) {
        warehouseRepository.deleteById(id);
    }
}
