package com.tasc.productservice.models;

import lombok.*;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearch {
    private Long totalQuantity;
    private List<Category> categories;
}
