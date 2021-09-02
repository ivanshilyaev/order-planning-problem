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
@IdClass(OrderPK.class)
public class Order {
    @Id
    @NotBlank(message = "Customer id is mandatory")
    private String customerId;

    @Id
    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @Column(name = "distance")
    private double distance;
}
