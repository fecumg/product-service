package com.tasc.productservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String description;

    private String uri;

    @OneToMany(mappedBy = "parent")
    private Collection<CategoryMapping> childCategoryMappings;

    @OneToMany(mappedBy = "child")
    private Collection<CategoryMapping> parentCategoryMappings;

    public Category(String name, String description, String uri) {
        this.name = name;
        this.description = description;
        this.uri = uri;
    }

    public Category(Date createdAt, Date updatedAt, int id, String name, String description, String uri) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.uri = uri;
    }
}
