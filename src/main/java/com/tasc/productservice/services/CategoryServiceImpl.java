package com.tasc.productservice.services;

import com.tasc.productservice.models.Category;
import com.tasc.productservice.models.CategoryMapping;
import com.tasc.productservice.models.Result;
import com.tasc.productservice.models.requests.CategoryRequest;
import com.tasc.productservice.models.responses.CategoryResponse;
import com.tasc.productservice.models.responses.ProductResponse;
import com.tasc.productservice.repositories.CategoryMappingRepository;
import com.tasc.productservice.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Truong Duc Duong
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMappingRepository categoryMappingRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(CategoryRequest categoryRequest) {
        Result result;
        try {
            Category category = modelMapper.map(categoryRequest, Category.class);
            Category newCategory = categoryRepository.save(category);
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
            List<CategoryMapping> categoryMappings = categoryMappingRepository.findAll();

            List<Integer> parentIds = categoryRequest.getParentIds();
            if (parentIds.isEmpty()) {
                result = new Result(0, "Success, 0 mappings added", categoryResponse);
            } else {
                int parentCount = 0;
                for (int parentId: parentIds
                ) {
                    Optional<Category> optionalParentCategory = categoryRepository.findById(parentId);
                    if (parentId != newCategory.getId() && optionalParentCategory.isPresent()) {
                        CategoryMapping categoryMapping = new CategoryMapping(parentId, newCategory.getId());
                        categoryMappingRepository.save(categoryMapping);
                        parentCount ++;
                    }
                }
                result = new Result(0, "Success, " + parentCount + " mapping(s) added", categoryResponse);
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(int id, CategoryRequest categoryRequest) {
        Result result;
        try {
            Category category = modelMapper.map(categoryRequest, Category.class);
            category.setId(id);
            categoryRepository.save(category);
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

            result = new Result(0, "Success", categoryResponse);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(int id) {
        Result result;
        try {
            List<CategoryMapping> categoryMappings = categoryMappingRepository.findAll();
            deleteStackedCategories(id, categoryMappings);

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
            List<Category> categories = categoryRepository.findAll();
            List<CategoryMapping> categoryMappings = categoryMappingRepository.findAll();
            if (categories.isEmpty()) {
                result = new Result(1, "No categories fetched");
            } else if (categoryMappings.isEmpty()) {
                result = new Result(1, "No category mappings fetched");
            } else {
                List<CategoryResponse> categoryResponses = modelMapper.map(categories, new TypeToken<List<ProductResponse>>() {}.getType());
                for (CategoryResponse categoryResponse: categoryResponses
                ) {
                    categoryResponse.setChildIds(getNextLevelChildIds(categoryResponse.getId(), categoryMappings));
                }
                result = new Result(0, "Data fetched", categoryResponses);
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result get(int id) {
        Result result;
        try {
            List<CategoryMapping> categoryMappings = categoryMappingRepository.findAll();
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                CategoryResponse categoryResponse = modelMapper.map(optionalCategory.get(), CategoryResponse.class);
                categoryResponse.setChildIds(getNextLevelChildIds(id, categoryMappings));

                result = new Result(0, "Success", categoryResponse);
            } else {
                result = new Result(1, "fail to retrieve data");
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getStackedCategories(int id) {
        Result result;
        try {
            List<CategoryMapping> mappings = categoryMappingRepository.findAll();
            List<Category>stackedCategories = stackCategories(id, mappings);

            result = new Result(0, "Success", stackedCategories);
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    private List<Category> stackCategories(int id, List<CategoryMapping> mappings) {
        List<Category> stackedCategories = new ArrayList<>();
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            stackedCategories.add(optionalCategory.get());

            for (CategoryMapping mapping: mappings
            ) {
                if (mapping.getParentId() == id) {
                    int childId = mapping.getChildId();
//                Recursion
                    stackedCategories.addAll(stackCategories(childId, mappings));
                }
            }
        }
        return stackedCategories;
    }

    private void deleteStackedCategories(int id, List<CategoryMapping> categoryMappings) {
        List<Integer> nextLevelChildIds = getNextLevelChildIds(id, categoryMappings);

//        browse all next level children's ids
        for (Integer nextLevelChildId: nextLevelChildIds
             ) {
//            if a child category does NOT have another parent, delete it
            if (!checkMultiParentCategory(nextLevelChildId, categoryMappings)) {
                categoryRepository.deleteById(nextLevelChildId);
            }
//            delete mappings that consist of deleted category's id and its children's ids
            CategoryMapping mapping = getMappingByChildAndParent(nextLevelChildId, id, categoryMappings);
            categoryMappingRepository.delete(mapping);

//            Recursion. Call the function itself and the parameter now is the next level children's ids
            deleteStackedCategories(nextLevelChildId, categoryMappings);
        }
    }

    private List<Integer> getNextLevelChildIds(int parentId, List<CategoryMapping> categoryMappings) {
        List<Integer> childIds = new ArrayList<>();

        for (CategoryMapping mapping: categoryMappings
        ) {
            if (mapping.getParentId() == parentId) {
                childIds.add(mapping.getChildId());
            }
        }
        return childIds;
    }

    private boolean checkMultiParentCategory(int id, List<CategoryMapping> categoryMappings) {
        int parentCount = 0;
        for (CategoryMapping mapping: categoryMappings
             ) {
            if (mapping.getChildId() == id) {
                parentCount ++;
            }
        }
        return parentCount >= 2;
    }

    private CategoryMapping getMappingByChildAndParent(int childId, int parentId, List<CategoryMapping> categoryMappings) {
        CategoryMapping categoryMapping = new CategoryMapping();
        for (CategoryMapping mapping: categoryMappings
            ) {
            if (mapping.getChildId() == childId && mapping.getParentId() == parentId) {
                categoryMapping = mapping;
            }
        }
        return categoryMapping;
    }
}
