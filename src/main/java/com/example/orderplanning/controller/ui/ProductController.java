package com.example.orderplanning.controller.ui;

import com.example.orderplanning.entity.Product;
import com.example.orderplanning.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/addProduct")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/addProduct";
    }

    @PostMapping("/addProduct")
    public String addProductSubmit(@ModelAttribute Product product) {
        productService.saveOrUpdate(product);
        return "redirect:/index";
    }

    @GetMapping("/allProducts")
    public String listAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/allProducts";
    }

    @GetMapping("/allProductNames")
    public String listAllProductNames(Model model) {
        model.addAttribute("productNames",
                productService.findAll().stream().map(Product::getName).distinct().collect(Collectors.toList()));
        return "product/allProductNames";
    }
}
