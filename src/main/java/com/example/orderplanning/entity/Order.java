package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Order {

    @Id
    private String id;

    private String customerId;

    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @Indexed
    private String warehouseId;

    private double distance;
}
