package com.example.demo.repository;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.OrderEntity;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
    // Add custom query methods here if needed
}
