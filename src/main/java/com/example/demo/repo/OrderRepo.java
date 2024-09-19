package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Order;
import com.example.demo.models.User;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
}
