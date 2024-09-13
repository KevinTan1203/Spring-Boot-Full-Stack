package com.example.demo.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.models.Product;
import com.example.demo.repo.ProductRepo;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductRepo productRepo;

    /*
     * When springboot creates an instance of the ProductController, it will
     * automatically
     * create an instance of the ProductRepo and pass it to the new instance of the
     * ProductController.
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

    /*
     * When we want to add forms, we always need 2 routes
     * 1 route to display the form
     * 1 route to process the form
     */
    @GetMapping("/products/create")
    public String showCreateProductForm(Model model) {
        var newProduct = new Product();
        model.addAttribute("product", newProduct); // Add the instance of the new product to the model
        return "/products/create";
    }

    // Result of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/products/create"; // Rerender the create form if there are any errors. Skip the saving of the product
        }
        // Save the new product to the database
        productRepo.save(newProduct);
        return "redirect:/products";
    }

    @GetMapping("/products/{id}") // URL parameter
    public String productDetails(@PathVariable Long id, Model model) {
        // Find the product with matching id
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid product Id" + id));
        model.addAttribute("product", product); // Add the product to the view model
        return "products/details"; // details.html in the products folder
    }

    @GetMapping("/products/{id}/edit")
    public String showUpdateProduct(@PathVariable Long id, Model model) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid product Id" + id));
        model.addAttribute("product", product);
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product) {
        product.setId(id);
        productRepo.save(product);
        return "redirect:/products";
    }
}
