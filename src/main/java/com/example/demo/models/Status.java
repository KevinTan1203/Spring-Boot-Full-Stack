package com.example.demo.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "status")
public class Status {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name cannot be blank")
  @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
  @Column(nullable = false, unique = true)
  private String name;

  public Status() {
  }

  public Status(String name) {
    this.name = name;
  }

  // Getters and setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  // toString method
  @Override
  public String toString() {
    return "Status [id=" + id + ", name=" + name + "]";
  }

  // equals and hashCode methods
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Status status = (Status) o;
    return Objects.equals(id, status.id) &&
        Objects.equals(name, status.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
