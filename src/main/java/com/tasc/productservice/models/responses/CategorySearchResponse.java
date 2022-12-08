package com.tasc.productservice.models.responses;

import lombok.*;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorySearchResponse {
    private Long totalQuantity;
    private List<CategoryResponse> categories;
}
