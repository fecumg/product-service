package com.tasc.productservice.services;

import com.tasc.productservice.models.CategoryMapping;
import com.tasc.productservice.models.Product;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.responses.ProductResponse;
import com.tasc.productservice.repositories.CategoryMappingRepository;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Service
public class CategoryMappingServiceImpl implements CategoryMappingService{
    @Autowired
    private CategoryMappingRepository categoryMappingRepository;

    @Override
    public Result save(CategoryMapping categoryMapping) {
        Result result;
        try {
            CategoryMapping newCategoryMapping = categoryMappingRepository.save(categoryMapping);
            result = new Result(0, "Success", newCategoryMapping);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result delete(int id) {
        Result result;
        try {
            categoryMappingRepository.deleteById(id);
            result = new Result(0, "Success");
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getAll() {
        Result result;
        try {
            List<CategoryMapping> mappings = categoryMappingRepository.findAll();
            result = new Result(0, "Data fetched", mappings);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result get(int id) {
        Result result;
        try {
            CategoryMapping categoryMapping = categoryMappingRepository.findById(id).get();
            result = new Result(0, "Success", categoryMapping);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }
}
