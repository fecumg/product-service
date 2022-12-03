package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.CategoryRequest;
import com.tasc.productservice.services.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/newCategory")
    public ResponseEntity<ApiDataResponse> create(@Valid @RequestBody CategoryRequest categoryRequest, BindingResult bindingResult) {
        Result result;
        if (bindingResult.hasErrors()) {
            return createResponse(getBindingErrorMessages(bindingResult), HttpStatus.BAD_REQUEST);
        } else {
            result = categoryService.save(categoryRequest);
            return createResponse(result);
        }
    }

    @GetMapping("/stackedCategories/{id}")
    public ResponseEntity<ApiDataResponse> getStackedCategories(@PathVariable("id") int id) {
        Result result = categoryService.getStackedCategories(id);
        return createResponse(result);
    }

    @PostMapping("/deleteCategory/{id}")
    public ResponseEntity<ApiDataResponse> deleteStackedCategories(@PathVariable("id") int id) {
        Result result = categoryService.delete(id);
        return createResponse(result);
    }
}
