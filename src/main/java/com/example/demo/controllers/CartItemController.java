package com.example.demo.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.models.CartItem;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.services.CartItemsService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;

@Controller
@RequestMapping("/cart")
public class CartItemController {
	private final CartItemsService cartItemsService;
	private final ProductService productService;
	private final UserService userService;

	@Autowired
	public CartItemController(CartItemsService cartItemsService, ProductService productService,
			UserService userService) {
		this.cartItemsService = cartItemsService;
		this.productService = productService;
		this.userService = userService;
	}

	@PostMapping("/add")
	public String addToCart(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
			Principal principal, RedirectAttributes redirectAttributes) {
		try {
			// When a route includes a principal in the parameter, the principal will
			// contain information about the currently logged in user
			// Redirect attributes are for the flash messages

			User user = userService.findUserByUsername(principal.getName());

			// Find the product by its ID
			// .orElseThrow() performs a .get(), and will throw an exception if the product
			// is not found
			Product product = productService.findById(productId)
					.orElseThrow(() -> new IllegalArgumentException("Product not found!"));
			cartItemsService.addToCart(user, product, quantity);
			redirectAttributes.addFlashAttribute(
					"message", String.format("Added %d %s to your cart", quantity, product.getName()));

			return "redirect:/cart";
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("Error when adding product: %s" + e.getMessage());
			return "redirect:/products";
		}
	}

	@GetMapping("")
	public String viewCart(Model model, Principal principal) {
		User user = userService.findUserByUsername(principal.getName());
		List<CartItem> cartItems = cartItemsService.findByUser(user);
		model.addAttribute("cartItems", cartItems);
		return "cart/index";
	}

	@PostMapping("/{cartItemId}/updateQuantity")
	public String updateQuantity(@PathVariable long cartItemId, @RequestParam int newQuantity,
			RedirectAttributes redirectAttributes, Principal principal) {
		try {
			User user = userService.findUserByUsername(principal.getName());
			cartItemsService.updateQuantity(cartItemId, user, newQuantity);
			redirectAttributes.addFlashAttribute("message", "Quantity updated");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Error updating quantity: " + e.getMessage());
		}
		return "redirect:/cart";
	}

	@GetMapping("/{cartItemId}/remove")
	public String removeFromCart(@PathVariable Long cartItemId,
			Principal principal,
			RedirectAttributes redirectAttributes) {
		try {
			User user = userService.findUserByUsername(principal.getName());
			cartItemsService.removeFromCart(cartItemId, user);
			redirectAttributes.addFlashAttribute("message", "Item removed from cart");
		} catch (IllegalArgumentException e) {
			redirectAttributes.addFlashAttribute("error", e.getMessage());
		}
		return "redirect:/cart";
	}
}
