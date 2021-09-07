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
@Table(name = "orders")
public class Order {

    private @Id
    @GeneratedValue
    Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @NotBlank(message = "Product name is mandatory")
    @Column(name = "product_name")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @Column(name = "distance")
    private double distance;
}
