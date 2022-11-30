package com.tasc.productservice.models.requests;

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
    private String barcode;
    private String name;
    private String image;
    private String description;
    private String content;
}
