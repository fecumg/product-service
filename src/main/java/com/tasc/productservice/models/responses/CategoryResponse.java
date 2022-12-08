package com.tasc.productservice.models.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.productservice.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "name", "description", "uri", "createdAt", "updatedAt" })
public class CategoryResponse extends BaseEntity {
    private int id;
    private String name, description, uri;
    private List<CategoryResponse> children = new ArrayList<>();
//    private List<CategoryResponse> parents = new ArrayList<>();
}
