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

    @Query(value = "select e from CustomerWarehouseDistance e join Product p on e.warehouse.id = p.warehouseId" +
            " where e.customer = ?1 and p.name = ?2 order by e.distance")
    Page<CustomerWarehouseDistance> findByCustomerAndProductName(Customer customer, String productName,
                                                                 Pageable pageable);

    Optional<CustomerWarehouseDistance> findByCustomerAndWarehouse(Customer customer, Warehouse warehouse);

    @Transactional
    @Modifying
    @Query(value = "delete from CustomerWarehouseDistance where customer = ?1")
    void deleteByCustomer(Customer customer);

    @Transactional
    @Modifying
    @Query(value = "delete from CustomerWarehouseDistance where warehouse = ?1")
    void deleteByWarehouse(Warehouse warehouse);
}
