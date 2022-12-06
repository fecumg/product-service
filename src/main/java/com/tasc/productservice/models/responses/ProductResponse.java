package com.tasc.productservice.models.responses;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tasc.productservice.models.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "barcode", "name", "image", "description", "content", "createdAt", "updatedAt" })
public class ProductResponse extends BaseEntity {
    private int id;
    private String barcode, name, image, description, content;
}
