package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.ProductModelAssembler;
import com.example.orderplanning.entity.Product;
import com.example.orderplanning.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService service;
    private final ProductModelAssembler assembler;
    private final PagedResourcesAssembler<Product> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Product>>> all(Pageable pageable) {
        Page<Product> page = service.findAll(pageable);
        PagedModel<EntityModel<Product>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(ProductController.class).all(pageable)).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Product>> newProduct(@Valid @RequestBody Product product) {
        service.save(product);
        EntityModel<Product> entityModel = assembler.toModel(product);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Product>> one(@PathVariable Long id) {
        return service.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"warehouseId", "name"})
    public ResponseEntity<CollectionModel<EntityModel<Product>>> search(@RequestParam Long warehouseId,
                                                                        @RequestParam String name
    ) {
        List<EntityModel<Product>> products = service.findByWarehouseIdAndName(warehouseId, name)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(products));
    }
}
