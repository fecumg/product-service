package com.tasc.productservice.repositories;

import com.tasc.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
}
