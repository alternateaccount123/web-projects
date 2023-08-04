package com.example.demo.repository;


import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartItemRepository extends CrudRepository<CartItem, Long> {

    List<CartItem> findByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);
}

