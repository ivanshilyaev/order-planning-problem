package com.example.orderplanning.assembler;

import com.example.orderplanning.controller.OrderController;
import com.example.orderplanning.entity.Order;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel(Order order) {
        return EntityModel.of(order,
                linkTo(methodOn(OrderController.class).one(order.getId()))
                        .withSelfRel(),
                linkTo(methodOn(OrderController.class).all()).withRel("orders"));
    }
}
