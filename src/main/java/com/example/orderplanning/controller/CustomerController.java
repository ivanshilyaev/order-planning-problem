package com.example.orderplanning.controller;

import com.example.orderplanning.assembler.CustomerModelAssembler;
import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.service.CustomerService;
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
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerModelAssembler assembler;
    private final PagedResourcesAssembler<Customer> pagedResourcesAssembler;

    @GetMapping("/customers")
    public ResponseEntity<CollectionModel<EntityModel<Customer>>> all(Pageable pageable) {
        Page<Customer> page = customerService.findAll(pageable);
        PagedModel<EntityModel<Customer>> model = pagedResourcesAssembler.toModel(page, assembler);

        return ResponseEntity.ok(CollectionModel.of(model,
                linkTo(methodOn(CustomerController.class).all(pageable)).withSelfRel()));
    }

    @PostMapping("/customers")
    public ResponseEntity<EntityModel<Customer>> newCustomer(@Valid @RequestBody Customer customer) {
        customerService.save(customer);
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
        Customer updatedCustomer = customerService.update(newCustomer, id);
        EntityModel<Customer> entityModel = assembler.toModel(updatedCustomer);

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
