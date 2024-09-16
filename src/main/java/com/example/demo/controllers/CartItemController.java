package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.demo.services.CartItemsService;
import com.example.demo.services.ProductService;
import com.example.demo.services.UserService;

@Controller
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

}
