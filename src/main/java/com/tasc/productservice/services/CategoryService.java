package com.tasc.productservice.services;

import com.tasc.productservice.models.CategoryMapping;
import com.tasc.productservice.models.Pagination;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.CategoryRequest;
import com.tasc.productservice.models.requests.ProductRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Service
public interface CategoryService {
    Result save(CategoryRequest categoryRequest);
    Result update(int id, CategoryRequest categoryRequest);
    Result delete(int id);
    Result getAll(Pagination pagination);
    Result get(int id);

    Result getStackedRootCategories(int id);

    Result getParentCategories(int id);
    Result getChildCategories(int id);

    Result searchByName(String text, Pagination pagination);
}
