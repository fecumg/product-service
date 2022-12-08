package com.tasc.productservice.models.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    @NotNull(message = "Name must be filled")
    @Size(min = 5, max = 100, message = "Name cannot be less than 5 or exceed 100 characters")
    private String name;

    @NotNull(message = "description must be filled")
    @Size(min = 5, max = 200, message = "description cannot be less than 5 or exceed 200 characters")
    private String description;

    @NotNull(message = "Uri must be filled")
    @Size(min = 5, max = 500, message = "Uri cannot be less than 5 or exceed 500 characters")
    private String uri;

    private List<Integer> parentIds;
}
