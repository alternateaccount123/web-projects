package com.example.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.OrderEntity;
import com.example.demo.repository.OrderEntityRepository;


@Service
public class OrderEntityService {

    private final OrderEntityRepository orderEntityRepository;

    @Autowired
    public OrderEntityService(OrderEntityRepository orderRepository) {
        this.orderEntityRepository = orderRepository;
    }

    public OrderEntity saveOrder(OrderEntity orderEntity) {
        return orderEntityRepository.save(orderEntity);
    }

    // Add other methods as needed for order-related operations
}
