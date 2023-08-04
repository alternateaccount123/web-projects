package com.example.demo.repository;



import com.example.demo.entity.Cart;
import com.example.demo.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find the cart associated with a specific user
    Cart findByUser(User user);

    // Add any custom query methods related to Cart entity if needed
}
