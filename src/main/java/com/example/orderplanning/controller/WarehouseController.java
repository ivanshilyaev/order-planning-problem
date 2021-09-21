package com.example.orderplanning.controller;

import com.example.orderplanning.entity.Warehouse;
import com.example.orderplanning.service.CustomerWarehouseDistanceService;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.WarehouseService;
import com.example.orderplanning.service.exception.NoWarehouseWithSuchIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RepositoryRestController
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final OrderPlanningService orderPlanningService;
    private final CustomerWarehouseDistanceService cwdService;

    @PostMapping("/warehouses")
    public ResponseEntity<EntityModel<Warehouse>> newWarehouse(@Valid @RequestBody Warehouse warehouse) {
        warehouseService.saveOrUpdate(warehouse);
        orderPlanningService.calculateDistanceToAllCustomers(warehouse);
        EntityModel<Warehouse> entityModel = EntityModel.of(warehouse);

        return ResponseEntity
                .created(linkTo(methodOn(WarehouseController.class).newWarehouse(warehouse)).withSelfRel().toUri())
                .body(entityModel);
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
        EntityModel<Warehouse> entityModel = EntityModel.of(updatedWarehouse);

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
