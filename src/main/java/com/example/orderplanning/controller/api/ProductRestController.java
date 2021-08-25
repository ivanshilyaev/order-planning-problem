package com.example.orderplanning.controller.api;

import com.example.orderplanning.entity.Product;
import com.example.orderplanning.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {
    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/addProduct")
    public Product addProduct(@RequestParam String warehouseId,
                              @RequestParam String name
    ) {
        Product product = new Product(warehouseId, name);
        productService.saveOrUpdate(product);
        return product;
    }

    @GetMapping("/api/allProducts")
    public List<Product> listAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/api/allProductNames")
    public List<String> listALlProductNames() {
        return productService.findAll().stream().map(Product::getName).distinct().collect(Collectors.toList());
    }
}