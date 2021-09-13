package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerWarehouseDistanceRepository extends JpaRepository<CustomerWarehouseDistance, Long> {

    List<CustomerWarehouseDistance> findByCustomer(Customer customer);

    @Query(value = "select e from CustomerWarehouseDistance e join Product p on e.warehouse.id = p.warehouseId" +
            " where e.customer = ?1 and p.name = ?2 order by e.distance")
    List<CustomerWarehouseDistance> findByCustomerAndProductName(Customer customer, String productName);

    Optional<CustomerWarehouseDistance> findByCustomerAndWarehouse(Customer customer, Warehouse warehouse);
}