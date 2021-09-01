package com.example.orderplanning.service;

import com.example.orderplanning.dao.OrderRepository;
import com.example.orderplanning.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void saveOrUpdate(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findByCustomerIdAndProductName(String customerId, String productName) {
        return orderRepository.findByCustomerIdAndProductName(customerId, productName);
    }
}
