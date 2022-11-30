package com.tasc.productservice.services;

import com.tasc.productservice.models.Product;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.ProductRequest;
import com.tasc.productservice.models.responses.ProductResponse;

import java.util.List;

/**
 * @author Truong Duc Duong
 */
public interface ProductService{
    Result save(ProductRequest productRequest);
    Result update(int id, ProductRequest productRequest);
    Result delete(int id);
    Result getAll();
    Result get(int id);
}
