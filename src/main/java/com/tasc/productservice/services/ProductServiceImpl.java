package com.tasc.productservice.services;

import com.tasc.productservice.models.Product;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.ProductRequest;
import com.tasc.productservice.models.responses.ProductResponse;
import com.tasc.productservice.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Service
@Component
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Result save(ProductRequest productRequest) {
        ProductResponse productResponse;
        Result result;
        try {
            Product product = modelMapper.map(productRequest, Product.class);
            productRepository.save(product);
            productResponse = modelMapper.map(product, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result update(int id, ProductRequest productRequest) {
        ProductResponse productResponse;
        Result result;
        try {
            Product product = modelMapper.map(productRequest, Product.class);
            product.setId(id);
            productRepository.save(product);
            productResponse = modelMapper.map(product, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
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
        List<ProductResponse> productResponses;
        Result result;
        try {
            List<Product> products = (List<Product>) productRepository.findAll();
            productResponses = modelMapper.map(products, new TypeToken<List<ProductResponse>>() {}.getType());
            result = new Result(0, "Data fetched", productResponses);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result get(int id) {
        ProductResponse productResponse;
        Result result;
        try {
            Product product = productRepository.findById(id).get();
            productResponse = modelMapper.map(product, ProductResponse.class);
            result = new Result(0, "Success", productResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }
}
