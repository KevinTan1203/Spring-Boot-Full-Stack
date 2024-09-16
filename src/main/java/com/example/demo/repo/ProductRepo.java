package com.example.demo.repo;

import com.example.demo.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
	@Query("SELECT DISTINCT p FROM Product p " +
			"LEFT JOIN FETCH p.category " +
			"LEFT JOIN FETCH p.tags")
	List<Product> findAllWithCategoriesAndTags();
}
