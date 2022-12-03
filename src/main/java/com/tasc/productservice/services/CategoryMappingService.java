package com.tasc.productservice.services;

import com.tasc.productservice.models.Category;
import com.tasc.productservice.models.CategoryMapping;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.CategoryRequest;

import java.util.List;

/**
 * @author Truong Duc Duong
 */
public interface CategoryMappingService {
    Result save(CategoryMapping categoryMapping);
    Result delete(int id);
    Result getAll();
    Result get(int id);


}
