package com.example.orderplanning.dao;

import com.example.orderplanning.entity.CustomerWarehouseDistance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CustomerWarehouseDistanceRepository extends MongoRepository<CustomerWarehouseDistance, String> {

    List<CustomerWarehouseDistance> findByCustomerId(String customerId);

    List<CustomerWarehouseDistance> findByWarehouseId(String warehouseId);

    @Query(value = "select e from CustomerWarehouseDistance e join Product p on e.warehouse.id = p.warehouseId" +
            " where e.customerId = ?1 and p.name = ?2 order by e.distance")
    Page<CustomerWarehouseDistance> findByCustomerIdAndProductName(String customerId, String productName,
                                                                   Pageable pageable);

    Optional<CustomerWarehouseDistance> findByCustomerIdAndWarehouseId(String customerId, String warehouseId);

    @Transactional
    @Query(value = "delete from CustomerWarehouseDistance where customerId = ?1")
    void deleteByCustomer(String customerId);

    @Transactional
    @Query(value = "delete from CustomerWarehouseDistance where warehouseId = ?1")
    void deleteByWarehouse(String warehouseId);
}
