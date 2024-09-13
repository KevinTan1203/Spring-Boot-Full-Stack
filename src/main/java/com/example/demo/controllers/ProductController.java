package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepo;

@Controller
public class ProductController {
    private final ProductRepo productRepo;

    /*
     * When springboot creates an instance of the ProductController, it will automatically
     * create an instance of the ProductRepo and pass it to the new instance of the ProductController.
    */
    @Autowired
    public ProductController(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @GetMapping("/products")
    public String listProduct(Model model) {
        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products); // Add the instance of the new product list to the model
        return "products/index";
    }

    /*  When we want to add forms, we always need 2 routes
     * 1 route to display the form
     * 1 route to process the form
    */
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        var newProduct = new Product();        
        model.addAttribute("product", newProduct); // Add the instance of the new product to the model
        return "/products/create";
    }
}
