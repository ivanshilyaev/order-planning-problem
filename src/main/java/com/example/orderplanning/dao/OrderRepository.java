package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.OrderPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, OrderPK> {

    Optional<Order> findByCustomerIdAndProductName(String customerId, String productName);
}
