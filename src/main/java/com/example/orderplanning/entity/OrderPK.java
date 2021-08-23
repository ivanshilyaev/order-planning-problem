package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class OrderPK implements Serializable {
    private String customer_id;
    private String product_name;
}
