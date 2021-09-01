package com.example.orderplanning.assembler;

import com.example.orderplanning.controller.ProductController;
import com.example.orderplanning.entity.Product;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProductModelAssembler implements RepresentationModelAssembler<Product, EntityModel<Product>> {

    @Override
    public EntityModel<Product> toModel(Product product) {

        return EntityModel.of(product,
                linkTo(methodOn(ProductController.class).one(product.getWarehouseId(), product.getName()))
                        .withSelfRel(),
                linkTo(methodOn(ProductController.class).all()).withRel("products"));
    }
}
