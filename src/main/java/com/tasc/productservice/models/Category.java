package com.tasc.productservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull(message = "Name must be filled")
    @Size(min = 5, max = 100, message = "Name cannot be less than 5 or exceed 100 characters")
    private String name;

    @NotNull(message = "description must be filled")
    @Size(min = 5, max = 200, message = "description cannot be less than 5 or exceed 200 characters")
    private String description;

    @NotNull(message = "Uri must be filled")
    @Size(min = 5, max = 500, message = "Uri cannot be less than 5 or exceed 500 characters")
    private String uri;

    public Category(String name, String description, String uri) {
        this.name = name;
        this.description = description;
        this.uri = uri;
    }
}
