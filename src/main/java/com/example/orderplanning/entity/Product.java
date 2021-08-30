package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@IdClass(ProductPK.class)
public class Product {
    @Id
    @NotBlank(message = "Warehouse id is mandatory")
    private String warehouseId;

    @Id
    @NotBlank(message = "Name is mandatory")
    private String name;
}
