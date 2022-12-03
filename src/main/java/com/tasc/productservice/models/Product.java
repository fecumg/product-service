package com.tasc.productservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String barcode;

    private String name;

    private String image;

    private String description;
    private String content;

    public Product(String barcode, String name, String image, String description, String content) {
        this.barcode = barcode;
        this.name = name;
        this.image = image;
        this.description = description;
        this.content = content;
    }
}
