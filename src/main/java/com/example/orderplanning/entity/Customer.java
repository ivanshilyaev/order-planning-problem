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
@Table(name = "customers")
public class Customer {

    private @Id
    @GeneratedValue
    Long id;

    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    private String name;

    @Column(name = "x")
    private int x;

    @Column(name = "y")
    private int y;
}
