package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    private @Id
    @GeneratedValue
    Long id;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    private String name;
}
