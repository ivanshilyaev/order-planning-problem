package com.example.orderplanning.service;

import com.example.orderplanning.dao.OrderRepository;
import com.example.orderplanning.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPlanningService orderPlanningService;

    @Transactional
    public void save(Order order) {
        orderPlanningService.findNearestWarehouse(order);
        orderRepository.save(order);
    }

    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findByCustomerIdAndProductName(Long customerId, String productName) {
        return orderRepository.findByCustomerIdAndProductName(customerId, productName);
    }
}
