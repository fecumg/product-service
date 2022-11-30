package com.tasc.productservice.controllers;

import com.tasc.productservice.models.ApiDataResponse;
import com.tasc.productservice.models.Product;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.ProductRequest;
import com.tasc.productservice.models.responses.ProductResponse;
import com.tasc.productservice.services.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@RestController
public class ProductController extends BaseController {
    @Autowired
    private ProductServiceImpl productService;


    @PostMapping("/newProduct")
    public ResponseEntity<ApiDataResponse> create(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        ApiDataResponse apiDataResponse;
        List<String> messages = new ArrayList<>();
        Result result;
        HttpStatus httpStatus;

        if (bindingResult.hasErrors()) {
            for (ObjectError error: bindingResult.getAllErrors()
                 ) {
                messages.add(error.getDefaultMessage());
            }
            httpStatus = HttpStatus.BAD_REQUEST;
            apiDataResponse = new ApiDataResponse(messages);
        } else {
            result = productService.save(productRequest);
            if (result.getCode() == 0) {
                messages.add("Success");
                httpStatus = HttpStatus.OK;
                apiDataResponse = new ApiDataResponse(messages, result.getData());
            } else {
                messages.add("Errors occurred");
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
                apiDataResponse = new ApiDataResponse(messages);
            }
        }
        return createResponse(apiDataResponse, httpStatus);
    }

    @GetMapping("/products")
    public ResponseEntity<ApiDataResponse> getAll() {
        ApiDataResponse apiDataResponse;
        List<String> messages = new ArrayList<>();
        Result result;
        HttpStatus httpStatus;

        result = productService.getAll();
        if (result.getCode() == 0) {
            messages.add("Data acquired");
            httpStatus = HttpStatus.OK;
            apiDataResponse = new ApiDataResponse(messages, result.getData());
        } else {
            messages.add(result.getMessage());
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            apiDataResponse = new ApiDataResponse(messages);
        }
        return createResponse(apiDataResponse, httpStatus);
    }
}
