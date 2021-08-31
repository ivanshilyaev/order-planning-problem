package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.CustomerModelAssembler;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
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
public class CustomerController {
    private final CustomerService service;
    private final CustomerModelAssembler assembler;

    public CustomerController(CustomerService service, CustomerModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> all() {
        List<EntityModel<Customer>> customers = service.findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel()));
    }

    @PostMapping("/customers")
    public ResponseEntity<?> newCustomer(@Valid @RequestBody Customer customer) {
        service.saveOrUpdate(customer);
        EntityModel<Customer> entityModel = assembler.toModel(customer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<EntityModel<Customer>> one(@PathVariable String id) {
        return service.findById(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer newCustomer, @PathVariable String id) {
        Customer updatedCustomer = service.findById(id)
                .map(customer -> {
                    customer.setX(newCustomer.getX());
                    customer.setY(newCustomer.getY());
                    service.saveOrUpdate(customer);
                    return customer;
                }).orElseGet(() -> {
                    newCustomer.setId(id);
                    service.saveOrUpdate(newCustomer);
                    return newCustomer;
                });
        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String id) {
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
