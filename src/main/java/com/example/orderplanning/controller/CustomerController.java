package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.CustomerModelAssembler;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
import com.example.orderplanning.service.OrderPlanningService;
import lombok.RequiredArgsConstructor;
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
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerModelAssembler assembler;
    private final OrderPlanningService orderPlanningService;

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> all() {
        List<EntityModel<Customer>> customers = customerService.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel()));
    }

    @PostMapping("/customers")
    public ResponseEntity<EntityModel<Customer>> newCustomer(@Valid @RequestBody Customer customer) {
        customerService.saveOrUpdate(customer);
        orderPlanningService.calculateDistanceToAllWarehouses(customer);
        EntityModel<Customer> entityModel = assembler.toModel(customer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> one(@PathVariable Long id) {
        return customerService.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> updateCustomer(@Valid @RequestBody Customer newCustomer,
                                                                @PathVariable Long id
    ) {
        Customer updatedCustomer = customerService.findById(id)
                .map(customer -> {
                    customer.setX(newCustomer.getX());
                    customer.setY(newCustomer.getY());
                    customerService.saveOrUpdate(customer);
                    return customer;
                }).orElseGet(() -> {
                    newCustomer.setId(id);
                    customerService.saveOrUpdate(newCustomer);
                    return newCustomer;
                });
        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);
        orderPlanningService.calculateDistanceToAllWarehouses(updatedCustomer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        customerService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
