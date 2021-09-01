package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Product;
import com.example.orderplanning.entity.ProductPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, ProductPK> {

    Optional<Product> findByWarehouseIdAndName(String warehouseId, String name);
}
