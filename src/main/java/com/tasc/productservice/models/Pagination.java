package com.tasc.productservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

/**
 * @author Truong Duc Duong
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagination {
    private int pageNumber = 1;
    private int pageSize = 10;
    private String sortBy = "id";
    private Sort.Direction direction = Sort.Direction.valueOf("ASC");
}
