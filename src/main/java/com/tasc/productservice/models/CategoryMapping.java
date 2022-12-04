package com.tasc.productservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "category_mappings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int parentId, childId;

    public CategoryMapping(int parentId, int childId) {
        this.parentId = parentId;
        this.childId = childId;
    }
}
