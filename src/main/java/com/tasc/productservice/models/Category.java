package com.tasc.productservice.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(max = 100)
    private String name;

    @NotNull
    @Size(max = 200)
    private String description;

    @NotNull
    @Size(max = 500)
    private String uri;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<CategoryMapping> childCategoryMappings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "child")
    private List<CategoryMapping> parentCategoryMappings;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_mappings",
            joinColumns = @JoinColumn(name = "child_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id")
    )
    private List<Category> parents;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "parents")
    private List<Category> children;


    public Category(Date createdAt, Date updatedAt, int id, String name, String description, String uri) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.uri = uri;
    }
}
