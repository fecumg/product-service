package com.tasc.productservice.models.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.productservice.models.BaseEntity;
import lombok.*;

/**
 * @author Truong Duc Duong
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "barcode", "name", "image", "description", "content", "createdAt", "updatedAt" })
public class ProductResponse extends BaseEntity {
    private int id;
    private String barcode, name, image, description, content;
}
