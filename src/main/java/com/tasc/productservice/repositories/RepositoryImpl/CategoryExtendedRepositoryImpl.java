package com.tasc.productservice.repositories.RepositoryImpl;

import com.tasc.productservice.models.Category;
import com.tasc.productservice.models.CategorySearch;
import com.tasc.productservice.repositories.CategoryExtendedRepository;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Truong Duc Duong
 */

@Repository
public class CategoryExtendedRepositoryImpl implements CategoryExtendedRepository {
    @PersistenceContext
    Session session;

    @Override
    public CategorySearch searchByName(String searchText, PageRequest pageRequest) {
        StringBuilder baseSql = new StringBuilder();
        baseSql.append(" from Category as c where 1 = 1");

        if (StringUtils.isNotBlank(searchText)) {
            baseSql.append(" and c.name like '%").append(searchText).append("%'");
        }

//        Set order property and direction
        String orderProperty = pageRequest.getSort().toString().split(": ")[0];
        String orderDirection = pageRequest.getSort().toString().split(":")[1];

        baseSql.append(" order by ").append(orderProperty).append(" ").append(orderDirection);

//        query to return total quantity
        String countSql = " select count(c)" + baseSql;
        Query countQuery = session.createQuery(countSql, Long.class);
        Object quantityObject = countQuery.getSingleResult();
        long quantity = 0;
//        cast quantityObject to long
        if (quantityObject instanceof BigInteger) {
            BigInteger bigIntQuantity = (BigInteger) quantityObject;
            quantity = bigIntQuantity.longValue();
        } else if (quantityObject instanceof Number) {
            quantity = (long) quantityObject;
        }

//        query to return categories
        String querySql = " select new Category (c.createdAt, c.updatedAt, c.id, c.name, c.description, c.uri)" + baseSql;
        Query resultQuery = session.createQuery(querySql, Category.class);
//        set page size and first result
        int size = pageRequest.getPageSize();
        int firstResult = (pageRequest.getPageNumber()) * pageRequest.getPageSize();
        resultQuery.setMaxResults(size).setFirstResult(firstResult);

        @SuppressWarnings(value = "unchecked")
        List<Category> categories = resultQuery.getResultList();

        return new CategorySearch(quantity, categories);
    }
}
