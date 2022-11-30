package com.tasc.productservice.repositories;

import com.tasc.productservice.models.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {

}
