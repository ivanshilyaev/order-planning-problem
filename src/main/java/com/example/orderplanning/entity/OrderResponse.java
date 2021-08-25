package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Warehouse warehouse;
    private double distance;
}
