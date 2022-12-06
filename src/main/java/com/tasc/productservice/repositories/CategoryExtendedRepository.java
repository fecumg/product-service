package com.tasc.productservice.repositories;

import com.tasc.productservice.models.Category;
import com.tasc.productservice.models.CategorySearch;
import com.tasc.productservice.models.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface CategoryExtendedRepository {
    CategorySearch searchByName(String searchText, PageRequest pageRequest);
}
