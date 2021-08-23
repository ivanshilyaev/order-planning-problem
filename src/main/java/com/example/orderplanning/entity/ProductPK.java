package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ProductPK implements Serializable {
    private String warehouseId;
    private String name;
}
