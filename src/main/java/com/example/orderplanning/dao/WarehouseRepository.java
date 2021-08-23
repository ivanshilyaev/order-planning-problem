package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, String> {

}
