package com.example.orderplanning.service;

import com.example.orderplanning.dao.ProductRepository;
import com.example.orderplanning.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void saveOrUpdate(Product product) {
        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findByWarehouseIdAndName(String warehouseId, String name) {
        return productRepository.findByWarehouseIdAndName(warehouseId, name);
    }
}
