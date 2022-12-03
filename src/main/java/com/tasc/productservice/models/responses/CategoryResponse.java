package com.tasc.productservice.models.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private int id;
    private String name, description, uri;
    private List<Integer> childIds;
}
