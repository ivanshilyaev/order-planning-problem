package com.example.orderplanning.assembler;

import com.example.orderplanning.controller.WarehouseController;
import com.example.orderplanning.entity.Warehouse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class WarehouseModelAssembler implements RepresentationModelAssembler<Warehouse, EntityModel<Warehouse>> {

    @Override
    public EntityModel<Warehouse> toModel(Warehouse warehouse) {

        return EntityModel.of(warehouse,
                linkTo(methodOn(WarehouseController.class).one(warehouse.getId())).withSelfRel());
    }
}
