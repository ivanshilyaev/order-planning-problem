package com.example.orderplanning.service;

import com.example.orderplanning.dao.ProductRepository;
import com.example.orderplanning.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveOrUpdate(Product product) {
        productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findByWarehouseIdAndName(Long warehouseId, String name) {
        return productRepository.findByWarehouseIdAndName(warehouseId, name);
    }
}
