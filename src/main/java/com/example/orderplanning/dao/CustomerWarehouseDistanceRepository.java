package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Customer;
import com.example.orderplanning.entity.CustomerWarehouseDistance;
import com.example.orderplanning.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerWarehouseDistanceRepository extends JpaRepository<CustomerWarehouseDistance, Long> {

    List<CustomerWarehouseDistance> findByCustomer(Customer customer);

    List<CustomerWarehouseDistance> findByWarehouse(Warehouse warehouse);

    @Query(value = "SELECT e FROM CustomerWarehouseDistance e JOIN Product p ON e.warehouse.id = p.warehouseId" +
            " WHERE e.customer = ?1 AND p.name = ?2 ORDER BY e.distance")
    Page<CustomerWarehouseDistance> findByCustomerAndProductName(Customer customer, String productName,
                                                                 Pageable pageable);

    Optional<CustomerWarehouseDistance> findByCustomerAndWarehouse(Customer customer, Warehouse warehouse);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CustomerWarehouseDistance WHERE customer = ?1")
    void deleteByCustomer(Customer customer);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CustomerWarehouseDistance WHERE warehouse = ?1")
    void deleteByWarehouse(Warehouse warehouse);
}
