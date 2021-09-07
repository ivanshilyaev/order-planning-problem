package com.example.orderplanning.service;

import com.example.orderplanning.dao.OrderRepository;
import com.example.orderplanning.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public void saveOrUpdate(Order order) {
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findByCustomerIdAndProductName(Long customerId, String productName) {
        return orderRepository.findByCustomerIdAndProductName(customerId, productName);
    }
}
