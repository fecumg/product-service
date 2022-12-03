package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.ProductRequest;
import com.tasc.productservice.services.ProductServiceImpl;
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
public class ProductController extends BaseController {
    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/newProduct")
    public ResponseEntity<ApiDataResponse> create(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        Result result;
        if (bindingResult.hasErrors()) {
            return createBindingErrorResponse(bindingResult);
        } else {
            result = productService.save(productRequest);
            return createResponse(result);
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ApiDataResponse> getAll() {
        Result result;
        result = productService.getAll();
        return createResponse(result);
    }
}
