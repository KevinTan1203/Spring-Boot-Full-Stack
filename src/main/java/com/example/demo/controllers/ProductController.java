package com.example.demo.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.models.Product;
import com.example.demo.models.Tag;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ProductRepo;
import com.example.demo.repo.TagRepo;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/products")
public class ProductController {
	private final ProductRepo productRepo;
	private final CategoryRepo categoryRepo;
	private final TagRepo tagRepo;

	/*
	 * When springboot creates an instance of the ProductController, it will
	 * automatically
	 * create an instance of the ProductRepo and pass it to the new instance of the
	 * ProductController.
	 */
	@Autowired
	public ProductController(ProductRepo productRepo, CategoryRepo categoryRepo, TagRepo tagRepo) {
		this.productRepo = productRepo;
		this.categoryRepo = categoryRepo;
		this.tagRepo = tagRepo;
	}

	@GetMapping("")
	public String listProduct(Model model) {
		List<Product> products = productRepo.findAllWithCategoriesAndTags();
		model.addAttribute("products", products); // Add the instance of the new product list to the model
		return "products/index";
	}

	/*
	 * When we want to add forms, we always need 2 routes
	 * 1 route to display the form
	 * 1 route to process the form
	 */
	@GetMapping("/create")
	public String showCreateProductForm(Model model) {
		var newProduct = new Product();
		model.addAttribute("product", newProduct); // Add the instance of the new product to the model
		model.addAttribute("categories", categoryRepo.findAll());
		model.addAttribute("allTags", tagRepo.findAll());
		return "/products/create";
	}

	// Result of the validation will be in the bindingResult parameter
	@PostMapping("/create")
	public String processCreateProductForm(@Valid @ModelAttribute Product newProduct,
			@RequestParam(required = false) List<Long> tagIds, BindingResult bindingResult,
			Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("categories", categoryRepo.findAll());
			model.addAttribute("allTags", tagRepo.findAll());
			return "/products/create"; // Rerender the create form if there are any errors. Skip the saving of the
																	// product
		}

		if (tagIds != null) {
			Set<Tag> tags = new HashSet<>(tagRepo.findAllById(tagIds));
			newProduct.setTags(tags);
		}

		// Save the new product to the database
		productRepo.save(newProduct);
		return "redirect:/products";
	}

	@GetMapping("/{id}") // URL parameter
	public String productDetails(@PathVariable Long id, Model model) {
		// Find the product with matching id
		var product = productRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid product Id" + id));
		model.addAttribute("product", product); // Add the product to the view model
		return "products/details"; // details.html in the products folder
	}

	@GetMapping("/{id}/edit")
	public String showUpdateProduct(@PathVariable Long id, Model model) {
		var product = productRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid product Id" + id));
		model.addAttribute("product", product);
		model.addAttribute("categories", categoryRepo.findAll());
		model.addAttribute("allTags", tagRepo.findAll());
		return "products/edit";
	}

	@PostMapping("/{id}/edit")
	public String updateProduct(@PathVariable Long id, @RequestParam(required = false) List<Long> tagIds,
			@Valid @ModelAttribute Product product, Model model,
			BindingResult bindingResult) {
		product.setId(id);

		if (bindingResult.hasErrors()) {
			model.addAttribute("product", product);
			model.addAttribute("categories", categoryRepo.findAll());
			model.addAttribute("allTags", tagRepo.findAll());
			return "redirect:/products/" + id + "/edit";
		}

		if (tagIds != null && !tagIds.isEmpty()) {
			Set<Tag> tags = new HashSet<Tag>(tagRepo.findAllById(tagIds));
			product.setTags(tags);
		} else {
			// remove all existing tags
			product.getTags().clear();
		}
		productRepo.save(product);
		return "redirect:/products";
	}

	/*
	 * Two routes for deleting
	 * 1. Show a delete form (asking the users if they really want to delete)
	 * 2. Process the delete
	 */
	@GetMapping("/{id}/delete")
	public String showDeleteProductForm(@PathVariable Long id, Model model) {
		var product = productRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("Invalid product Id" + id));

		model.addAttribute("product", product);
		return "products/delete";
	}

	@PostMapping("/{id}/delete")
	public String deleteProduct(@PathVariable Long id) {
		productRepo.deleteById(id);
		return "redirect:/products";
	}
}
