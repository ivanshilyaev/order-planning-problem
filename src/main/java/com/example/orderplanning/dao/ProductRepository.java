package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Product;
import com.example.orderplanning.entity.ProductPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, ProductPK> {
}
