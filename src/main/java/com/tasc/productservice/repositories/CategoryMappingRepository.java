package com.tasc.productservice.repositories;

import com.tasc.productservice.models.CategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface CategoryMappingRepository extends JpaRepository<CategoryMapping, Integer> {
}
