package com.tasc.productservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class CategoryMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonManagedReference
    @ManyToOne()
    @JoinColumn(name = "parent_id")
    private Category parent ;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Category child;

}
