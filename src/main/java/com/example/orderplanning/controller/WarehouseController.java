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
@RequestMapping("/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseModelAssembler assembler;
    private final PagedResourcesAssembler<Warehouse> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Warehouse>>> all(Pageable pageable) {
        Page<Warehouse> page = warehouseService.findAll(pageable);
        PagedModel<EntityModel<Warehouse>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(WarehouseController.class).all(pageable)).withSelfRel()));
    }

    @PostMapping
    public ResponseEntity<EntityModel<Warehouse>> newWarehouse(@Valid @RequestBody Warehouse warehouse) {
        warehouseService.save(warehouse);
        EntityModel<Warehouse> entityModel = assembler.toModel(warehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Warehouse>> one(@PathVariable Long id) {
        return warehouseService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Warehouse>> updateWarehouse(@Valid @RequestBody Warehouse newWarehouse,
                                                                  @PathVariable Long id
    ) {
        Warehouse updatedWarehouse = warehouseService.update(newWarehouse, id);
        EntityModel<Warehouse> entityModel = assembler.toModel(updatedWarehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        warehouseService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
