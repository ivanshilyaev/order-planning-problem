package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.OrderModelAssembler;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderPlanningService orderPlanningService;
    private final OrderModelAssembler assembler;

    @GetMapping("/orders")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> all() {
        List<EntityModel<Order>> orders = orderService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel()));
    }

    // returns the nearest to the customer warehouse,
    // containing the product, and distance to that warehouse
    @PostMapping("/orders")
    public ResponseEntity<EntityModel<Order>> newOrder(@Valid @RequestBody Order order) {
        orderPlanningService.findNearestWarehouse(order);
        orderService.saveOrUpdate(order);
        EntityModel<Order> entityModel = assembler.toModel(order);

        return ResponseEntity
                .created(assembler.toModel(order).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<EntityModel<Order>> one(@PathVariable Long id) {
        return orderService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders?")
    public ResponseEntity<CollectionModel<EntityModel<Order>>> search(@RequestParam Long customerId,
                                                                      @RequestParam String productName
    ) {
        List<EntityModel<Order>> orders = orderService.findByCustomerIdAndProductName(customerId, productName)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orders,
                linkTo(methodOn(OrderController.class).all()).withSelfRel()));
    }
}
