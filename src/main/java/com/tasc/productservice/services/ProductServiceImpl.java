package com.tasc.productservice.services;

import com.tasc.productservice.models.Product;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.ProductRequest;
import com.tasc.productservice.models.responses.ProductResponse;
import com.tasc.productservice.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Result save(ProductRequest productRequest) {
        Result result;
        try {
            Product product = modelMapper.map(productRequest, Product.class);
            Product newProduct = productRepository.save(product);
            ProductResponse productResponse = modelMapper.map(newProduct, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Result update(int id, ProductRequest productRequest) {
        Result result;
        try {
            Product product = modelMapper.map(productRequest, Product.class);
            product.setId(id);
            productRepository.save(product);
            ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional
    public Result delete(int id) {
        Result result;
        try {
            productRepository.deleteById(id);
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
            List<Product> products = (List<Product>) productRepository.findAll();
            List<ProductResponse> productResponses = modelMapper.map(products, new TypeToken<List<ProductResponse>>() {}.getType());
            result = new Result(0, "Data fetched", productResponses);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result get(int id) {
        Result result;
        try {
            Product product = productRepository.findById(id).get();
            ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }
}
