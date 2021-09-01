package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.ProductModelAssembler;
import com.example.orderplanning.entity.Product;
import com.example.orderplanning.service.ProductService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
public class ProductController {
    private final ProductService service;
    private final ProductModelAssembler assembler;

    public ProductController(ProductService service, ProductModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/products")
    public ResponseEntity<CollectionModel<EntityModel<Product>>> all() {
        List<EntityModel<Product>> products = service.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(products,
                linkTo(methodOn(ProductController.class).all()).withSelfRel()));
    }

    @PostMapping("/products")
    public ResponseEntity<?> newProduct(@Valid @RequestBody Product product) {
        service.saveOrUpdate(product);
        EntityModel<Product> entityModel = assembler.toModel(product);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/products/{warehouseId}/{name}")
    public ResponseEntity<EntityModel<Product>> one(@PathVariable String warehouseId, @PathVariable String name) {
        return service.findByWarehouseIdAndName(warehouseId, name)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
