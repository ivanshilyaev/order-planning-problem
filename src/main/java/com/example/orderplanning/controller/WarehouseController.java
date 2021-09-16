package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.WarehouseModelAssembler;
import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.CustomerWarehouseDistanceService;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.WarehouseService;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
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
    private final WarehouseService warehouseService;
    private final OrderPlanningService orderPlanningService;
    private final CustomerWarehouseDistanceService cwdService;
    private final WarehouseModelAssembler assembler;
    private final PagedResourcesAssembler<Warehouse> pagedResourcesAssembler;

    @GetMapping("/warehouses")
    public ResponseEntity<CollectionModel<EntityModel<Warehouse>>> all(Pageable pageable) {
        Page<Warehouse> page = warehouseService.findAll(pageable);
        PagedModel<EntityModel<Warehouse>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(WarehouseController.class).all(pageable)).withSelfRel()));
    }

    @PostMapping("/warehouses")
    public ResponseEntity<EntityModel<Warehouse>> newWarehouse(@Valid @RequestBody Warehouse warehouse) {
        warehouseService.saveOrUpdate(warehouse);
        orderPlanningService.calculateDistanceToAllCustomers(warehouse);
        EntityModel<Warehouse> entityModel = assembler.toModel(warehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/warehouses/{id}")
    public ResponseEntity<EntityModel<Warehouse>> one(@PathVariable Long id) {
        return warehouseService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/warehouses/{id}")
    public ResponseEntity<EntityModel<Warehouse>> updateWarehouse(@Valid @RequestBody Warehouse newWarehouse,
                                                                  @PathVariable Long id
    ) {
        Warehouse updatedWarehouse = warehouseService.findById(id)
                .map(warehouse -> {
                    int previousX = warehouse.getX();
                    int previousY = warehouse.getY();
                    warehouse.setName(newWarehouse.getName());
                    warehouse.setX(newWarehouse.getX());
                    warehouse.setY(newWarehouse.getY());
                    warehouseService.saveOrUpdate(warehouse);
                    if (previousX != warehouse.getX() || previousY != warehouse.getY()) {
                        orderPlanningService.calculateDistanceToAllCustomers(warehouse);
                    }
                    return warehouse;
                }).orElseGet(() -> {
                    newWarehouse.setId(id);
                    warehouseService.saveOrUpdate(newWarehouse);
                    orderPlanningService.calculateDistanceToAllCustomers(newWarehouse);
                    return newWarehouse;
                });
        EntityModel<Warehouse> entityModel = assembler.toModel(updatedWarehouse);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/warehouses/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Warehouse warehouse = warehouseService.findById(id)
                .orElseThrow(() -> new NoWarehouseWithSuchIdException("No warehouse with id " + id));
        cwdService.deleteByWarehouse(warehouse);
        warehouseService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
