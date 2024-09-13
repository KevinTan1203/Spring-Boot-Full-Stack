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
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;

import jakarta.validation.Valid;

@Controller
public class ProductController {
    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    /*
     * When springboot creates an instance of the ProductController, it will
     * automatically
     * create an instance of the ProductRepo and pass it to the new instance of the
     * ProductController.
     */
    @Autowired
    public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo) {
        this.productRepo = productRepo;
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/products")
    public String listProduct(Model model) {
        List<Product> products = productRepo.findAllWithCategories();
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
        model.addAttribute("categories", categoryRepo.findAll());
        return "/products/create";
    }

    // Result of the validation will be in the bindingResult parameter
    @PostMapping("/products/create")
    public String processCreateProductForm(@Valid @ModelAttribute Product newProduct, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepo.findAll());
            return "/products/create"; // Rerender the create form if there are any errors. Skip the saving of the
                                       // product
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
        model.addAttribute("categories", categoryRepo.findAll());
        return "products/edit";
    }

    @PostMapping("/products/{id}/edit")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute Product product, Model model,
            BindingResult bindingResult) {
        product.setId(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepo.findAll());
            return "redirect:/products/" + id + "/edit";
        }
        productRepo.save(product);
        return "redirect:/products";
    }

    /*
     * Two routes for deleting
     * 1. Show a delete form (asking the users if they really want to delete)
     * 2. Process the delete
     */
    @GetMapping("/products/{id}/delete")
    public String showDeleteProductForm(@PathVariable Long id, Model model) {
        var product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid product Id" + id));

        model.addAttribute("product", product);
        return "products/delete";
    }

    @PostMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/products";
    }
}
