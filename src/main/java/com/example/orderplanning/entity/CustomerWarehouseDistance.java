package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class CustomerWarehouseDistance {

    @Id
    private String id;

    @Indexed
    private String customerId;

    @Indexed
    private String warehouseId;

    private double distance;
}
