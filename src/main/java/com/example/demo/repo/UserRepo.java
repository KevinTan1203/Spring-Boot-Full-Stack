package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	// Automated query generation
	User findByUsername(String username);
}
