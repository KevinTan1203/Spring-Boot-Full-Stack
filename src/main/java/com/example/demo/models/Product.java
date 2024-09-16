package com.example.demo.models;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(columnDefinition = "TEXT")
	private String description;

	@NotBlank(message = "Name is mandatory")
	@Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
	@Column(nullable = false)
	private String name;

	@DecimalMin(value = "0.01", message = "Price must be greater than 0.01")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	// Create the foreign key for the product
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "category_id", nullable = false) // Name is the column nmae in the table
	private Category category;

	// Add reference to the tags. To do so, we will use a set to contain unique tags
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "products_tags", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<Tag> tags = new HashSet<>();

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getProducts().add(this);
	}

	public void removeTag(Tag tag) {
		this.tags.remove(tag);
		tag.getProducts().remove(this);
	}

	public Set<Tag> getTags() {
		return tags;
	}

	public Product() {
	}

	public Product(String description, String name, BigDecimal price) {
		this.description = description;
		this.name = name;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", price=" + price +
				'}';
	}

	// equals and hashCode methods
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product) o;
		return Objects.equals(id, product.id) &&
				Objects.equals(name, product.name) &&
				Objects.equals(description, product.description) &&
				Objects.equals(price, product.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, price);
	}
}
