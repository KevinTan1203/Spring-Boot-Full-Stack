package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import java.util.List;
import com.example.demo.models.Product;


import com.example.demo.repo.ProductRepo;

@Controller
public class ProductController {
    private final ProductRepo productRepo;

    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public String listProduct(Model model) {
        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);
        return "products/index";
    }
    
}
