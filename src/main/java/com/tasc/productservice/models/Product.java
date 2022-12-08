package com.tasc.productservice.models;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Truong Duc Duong
 */

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String barcode;

    private String name;

    private String image;

    private String description;
    private String content;

}
