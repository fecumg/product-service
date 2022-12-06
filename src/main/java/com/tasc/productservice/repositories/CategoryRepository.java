package com.tasc.productservice.repositories;

import com.tasc.productservice.models.Category;
import com.tasc.productservice.models.Pagination;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.math.BigInteger;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, PagingAndSortingRepository<Category, Integer> {

    @Query(value =  "select new Category(c.createdAt, c.updatedAt, c.id, c.name, c.description, c.uri) " +
            "from Category as c" +
            " inner join CategoryMapping as cm" +
            " on c.id = cm.parent.id" +
            " where cm.child.id = ?1"
    )
    List<Category> findParents(int id);

    @Query(value = "select new Category(c.createdAt, c.updatedAt, c.id, c.name, c.description, c.uri) " +
            "from Category as c " +
            "inner join CategoryMapping as cm " +
            "on c.id = cm.child.id " +
            "where cm.parent.id = ?1"
    )
    List<Category> findChildren(int id);

    @Query(value = "select new Category(c.createdAt, c.updatedAt, c.id, c.name, c.description, c.uri) " +
            "from Category as c " +
            "where c.name like %?1%"
    )
    List<Category> searchByName(String searchText, PageRequest pageRequest);

    @Query(value = "select count(c) " +
            "from Category as c " +
            "where c.name like %?1%"
    )
    BigInteger searchByNameCount(String searchText);
}
