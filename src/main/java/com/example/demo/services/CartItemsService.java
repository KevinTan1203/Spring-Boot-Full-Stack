package com.example.demo.services;

import org.springframework.stereotype.Service;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repo.CartItemRepo;

import jakarta.transaction.Transactional;

@Service
public class CartItemsService {
	private final CartItemRepo cartItemRepo;

	public CartItemsService(CartItemRepo cartItemRepo) {
		this.cartItemRepo = cartItemRepo;
	}

	// When a method is transactional, if throws an exception, the transaction
	// For any reason, all database writes and updates will be undone.
	@Transactional
	public CartItem addToCart(User user, Product product, int quantity) {
		return null;
	}
}
