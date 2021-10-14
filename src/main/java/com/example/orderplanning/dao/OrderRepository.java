package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerIdAndProductName(Long customerId, String productName);
}
