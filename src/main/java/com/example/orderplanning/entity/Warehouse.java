package com.example.orderplanning.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Warehouse {

    @Id
    private String id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private int x;

    private int y;
}
