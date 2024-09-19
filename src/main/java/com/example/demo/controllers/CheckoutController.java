package com.example.demo.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.models.CartItem;
import com.example.demo.models.User;
import com.example.demo.services.CartItemsService;
import com.example.demo.services.StripeService;
import com.stripe.model.Review.Session;

import jakarta.el.ELException;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    private final StripeService stripeService;
    private final CartItemsService cartItemsService;

    public CheckoutController(StripeService stripeService, CartItemsService cartItemsService) {
        this.stripeService = stripeService;
        this.cartItemsService = cartItemsService;
    }

    @GetMapping("/create-checkout-session")
    public String createCheckoutSession(
            @AuthenticationPrincipal User user,
            Model model) {
        try {
            String successUrl = "https://www.google.com";
            String cancelUrl = "https://www.yahoo.com";

            var cartItems = cartItemsService.findByUser(user);
            var session = stripeService.createCheckOutSession(cartItems, user.getId(), successUrl, cancelUrl);

            model.addAttribute("sessionId", session.getId());
            model.addAttribute("stripePublicKey", stripeService.getPublicKey());
            return "checkout/checkout";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

}
