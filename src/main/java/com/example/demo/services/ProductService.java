package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepo;
import java.util.Optional;
import java.util.List;

@Service
public class ProductService {
	private final ProductRepo productRepo;

	@Autowired
	public ProductService(ProductRepo productRepo) {
		this.productRepo = productRepo;
	}

	public Optional<Product> findById(Long id) {
		return productRepo.findById(id);
	}

	public List<Product> findAllWithCategories() {
		return productRepo.findAllWithCategoriesAndTags();
	}
}
