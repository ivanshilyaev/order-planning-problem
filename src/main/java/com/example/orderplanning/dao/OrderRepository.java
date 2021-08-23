package com.example.orderplanning.dao;

import com.example.orderplanning.entity.Order;
import com.example.orderplanning.entity.OrderPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, OrderPK> {
}
