package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByWarehouseIdAndName(Long warehouseId, String name);
}
