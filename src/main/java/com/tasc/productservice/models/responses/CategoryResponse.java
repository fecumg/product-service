package com.tasc.productservice.models.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.productservice.models.BaseEntity;
import jakarta.persistence.Lob;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "name", "description", "uri", "createdAt", "updatedAt" })
public class CategoryResponse extends BaseEntity {
    private int id;
    private String name, description, uri;
    private List<CategoryResponse> children = new ArrayList<>();
//    private List<CategoryResponse> parents = new ArrayList<>();
}
