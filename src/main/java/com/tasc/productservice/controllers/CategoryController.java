package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Pagination;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.CategoryRequest;
import com.tasc.productservice.services.CategoryServiceImpl;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author Truong Duc Duong
 */

@RestController
public class CategoryController extends BaseController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @PostMapping(value = "/newCategory", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ApiDataResponse> create(@Valid @ModelAttribute CategoryRequest categoryRequest, BindingResult bindingResult) {
        Result result;
        if (bindingResult.hasErrors()) {
            return createBindingErrorResponse(bindingResult);
        } else {
            result = categoryService.save(categoryRequest);
            return createResponse(result);
        }
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiDataResponse> getAllCategories(@Nullable Pagination pagination) {
        Result result = categoryService.getAll(pagination);
        return createResponse(result);
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<ApiDataResponse> getCategory(@PathVariable("id") int id) {
        Result result = categoryService.get(id);
        return createResponse(result);
    }

    @GetMapping("/categoryTrees/{id}")
    public ResponseEntity<ApiDataResponse> getCategoryTrees(@PathVariable("id") int id) {
        Result result = categoryService.getStackedRootCategories(id);
        return createResponse(result);
    }

    @GetMapping("/parentCategories/{id}")
    public ResponseEntity<ApiDataResponse> getParentCategories(@PathVariable("id") int id) {
        Result result = categoryService.getParentCategories(id);
        return createResponse(result);
    }

    @GetMapping("/childCategories/{id}")
    public ResponseEntity<ApiDataResponse> getChildCategories(@PathVariable("id") int id) {
        Result result = categoryService.getChildCategories(id);
        return createResponse(result);
    }

    @GetMapping("/searchCategoriesByName")
    public ResponseEntity<ApiDataResponse> searchCategoriesByName(@RequestParam(name = "search", required = false) String search, @Nullable Pagination pagination) {
        Result result = categoryService.searchByName(search, pagination);
        return createResponse(result);
    }

    @PostMapping("/deleteCategory/{id}")
    public ResponseEntity<ApiDataResponse> deleteStackedCategories(@PathVariable("id") int id) {
        Result result = categoryService.delete(id);
        return createResponse(result);
    }
}
