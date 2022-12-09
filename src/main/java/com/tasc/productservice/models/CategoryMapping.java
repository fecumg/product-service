package com.tasc.productservice.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "category_mappings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent ;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Category child;

}
