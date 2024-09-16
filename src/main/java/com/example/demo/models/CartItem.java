package com.example.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// One user can own many cart items, but a cart item can only belong to one user
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	private int quantity;

	@ManyToOne
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	public CartItem() {
	}

	public CartItem(int quantity) {
		this.quantity = quantity;
	}
	
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}

	public BigDecimal getTotal() {
		return product.getPrice().multiply(new BigDecimal(quantity));
	}
}
