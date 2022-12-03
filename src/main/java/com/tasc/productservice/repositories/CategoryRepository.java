package com.tasc.productservice.repositories;

import com.tasc.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
