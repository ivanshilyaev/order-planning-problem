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
@Table(name = "orders")
@IdClass(OrderPK.class)
public class Order {
    @Id
    @NotBlank(message = "Customer id is mandatory")
    private String customerId;

    @Id
    @NotBlank(message = "Product name is mandatory")
    private String productName;
}
