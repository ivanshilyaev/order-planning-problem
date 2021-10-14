package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.OrderModelAssembler;
import com.example.orderplanning.entity.Order;
import com.example.orderplanning.service.OrderService;
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
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderModelAssembler assembler;
    private final PagedResourcesAssembler<Order> pagedResourcesAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Order>>> all(Pageable pageable) {
        Page<Order> page = orderService.findAll(pageable);
        PagedModel<EntityModel<Order>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(OrderController.class).all(pageable)).withSelfRel()));
    }

    /**
     * Finds the nearest to the customer warehouse, containing
     * the product, calculates distance to that warehouse,
     * and saves both distance and warehouse id in Order entity
     */
    @PostMapping
    public ResponseEntity<EntityModel<Order>> newOrder(@Valid @RequestBody Order order) {
        orderService.save(order);
        EntityModel<Order> entityModel = assembler.toModel(order);

        return ResponseEntity
                .created(assembler.toModel(order).getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Order>> one(@PathVariable Long id) {
        return orderService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(params = {"customerId", "productName"})
    public ResponseEntity<CollectionModel<EntityModel<Order>>> search(@RequestParam Long customerId,
                                                                      @RequestParam String productName
    ) {
        List<EntityModel<Order>> orders = orderService.findByCustomerIdAndProductName(customerId, productName)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(orders));
    }
}
