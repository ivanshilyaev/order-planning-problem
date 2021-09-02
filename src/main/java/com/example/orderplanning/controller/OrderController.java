package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.OrderModelAssembler;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.service.CustomerService;
import com.example.orderplanning.service.OrderPlanningService;
import com.example.orderplanning.service.OrderService;
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
public class OrderController {
    private final OrderService orderService;
    private final CustomerService customerService;
    private final OrderPlanningService orderPlanningService;
    private final OrderModelAssembler assembler;

    public OrderController(OrderService orderService,
                           CustomerService customerService,
                           OrderPlanningService orderPlanningService,
                           OrderModelAssembler assembler) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.orderPlanningService = orderPlanningService;
        this.assembler = assembler;
    }

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
    public ResponseEntity<?> newOrder(@Valid @RequestBody Order order) {
        orderPlanningService.findNearestWarehouse(order);
        orderService.saveOrUpdate(order);
        EntityModel<Order> entityModel = assembler.toModel(order);

        return ResponseEntity
                .created(assembler.toModel(order).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/orders/{customerId}/{productName}")
    public ResponseEntity<EntityModel<Order>> one(@PathVariable String customerId, @PathVariable String productName) {
        return orderService.findByCustomerIdAndProductName(customerId, productName)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
