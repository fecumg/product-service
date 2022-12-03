package com.tasc.productservice.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    @NotNull(message = "Barcode must be inserted or scanned")
    @Size(min = 5, max = 20, message = "Barcode cannot be less than 5 or exceed 20 characters")
    private String barcode;

    @NotNull(message = "Product must have a name")
    @Size(min = 5, max = 100, message = "Name cannot be less than 5 or exceed 100 characters")
    private String name;

    @NotNull(message = "product must have a promotion image")
    @Size(max = 1000, message = "Image cannot exceed 20 characters")
    private String image;

    @NotNull(message = "describe the product")
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull(message = "Product's content must be filled")
    @Size(max = 500, message = "Content cannot exceed 500 characters")
    private String content;

}
