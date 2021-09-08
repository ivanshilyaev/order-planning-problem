package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.WarehouseModelAssembler;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.WarehouseService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService service;
    private final WarehouseModelAssembler assembler;
    private final PagedResourcesAssembler<Warehouse> pagedResourcesAssembler;

    @GetMapping("/warehouses")
    public ResponseEntity<CollectionModel<EntityModel<Warehouse>>> all(Pageable pageable) {
        Page<Warehouse> page = service.findAll(pageable);
        PagedModel<EntityModel<Warehouse>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(WarehouseController.class).all(pageable)).withSelfRel()));
    }

    @PostMapping("/warehouses")
    public ResponseEntity<EntityModel<Warehouse>> newWarehouse(@Valid @RequestBody Warehouse warehouse) {
        service.saveOrUpdate(warehouse);
        EntityModel<Warehouse> entityModel = assembler.toModel(warehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity<EntityModel<Warehouse>> one(@PathVariable Long id) {
        return service.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/warehouses/{id}")
    public ResponseEntity<EntityModel<Warehouse>> updateWarehouse(@Valid @RequestBody Warehouse newWarehouse,
                                                                  @PathVariable Long id
    ) {
        Warehouse updatedWarehouse = service.findById(id)
                .map(warehouse -> {
                    warehouse.setX(newWarehouse.getX());
                    warehouse.setY(newWarehouse.getY());
                    service.saveOrUpdate(warehouse);
                    return warehouse;
                }).orElseGet(() -> {
                    newWarehouse.setId(id);
                    service.saveOrUpdate(newWarehouse);
                    return newWarehouse;
                });
        EntityModel<Warehouse> entityModel = assembler.toModel(updatedWarehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
