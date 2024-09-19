package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Status;

@Repository
public interface StatusRepo extends JpaRepository<Status, Long> {
  
}
