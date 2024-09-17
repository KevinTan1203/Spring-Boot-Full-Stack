package com.example.demo.services;

import java.util.Optional;
import java.util.List;

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
		// If an item is already in the cart
		Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(user, product);
		if (existingItem.isPresent()) {
			// To get the actual item from the Optional, we have to use .get()
			CartItem cartItem = existingItem.get();
			cartItem.setQuantity(cartItem.getQuantity() + 1);
			return cartItemRepo.save(cartItem);
		} else { // When the product is not in the cart
			CartItem cartItem = new CartItem(user, product, quantity);
			return cartItemRepo.save(cartItem);
		}
	}

	public List<CartItem> findByUser(User user) {
		return cartItemRepo.findByUser(user);
	}

	@Transactional
	public void updateQuantity(long cartItemId, User user, int newQuantity) {
		CartItem existingItem = cartItemRepo.findByUserAndId(user, cartItemId)
				.orElseThrow(() -> new IllegalArgumentException("No cart item with this id"));
		existingItem.setQuantity(newQuantity);
		cartItemRepo.save(existingItem);
	}

	@Transactional
	public void removeFromCart(long cartItemId, User user) {
		cartItemRepo.deleteByIdAndUser(cartItemId, user);
	}
}
