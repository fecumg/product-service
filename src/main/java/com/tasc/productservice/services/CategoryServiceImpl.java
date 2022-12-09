package com.tasc.productservice.services;

import com.tasc.productservice.models.*;
import com.tasc.productservice.models.requests.CategoryRequest;
import com.tasc.productservice.models.responses.CategoryResponse;
import com.tasc.productservice.models.responses.CategorySearchResponse;
import com.tasc.productservice.repositories.CategoryExtendedRepository;
import com.tasc.productservice.repositories.CategoryMappingRepository;
import com.tasc.productservice.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Truong Duc Duong
 */
@Service
@Log4j2
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMappingRepository categoryMappingRepository;
    @Autowired
    private CategoryExtendedRepository categoryExtendedRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result save(CategoryRequest categoryRequest) {
        Result result;
        try {
            Category category = modelMapper.map(categoryRequest, Category.class);

//            set parents
            int parentCount = setParents(categoryRequest, category);

//            save new category and mappings
            Category newCategory = categoryRepository.save(category);
            CategoryResponse categoryResponse = modelMapper.map(newCategory, CategoryResponse.class);

            result = new Result(0, "Success, " + parentCount + " parent mapping(s) added", categoryResponse);
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

//            check if category with requested id exists
            Optional<Category> optionalCurrentCategory = categoryRepository.findById(id);
            if (optionalCurrentCategory.isPresent()) {
                Category currentCategory = optionalCurrentCategory.get();

//                map requested category to Category type
                Category category = modelMapper.map(categoryRequest, Category.class);
                category.setId(id);

//                Delete all existing parent mappings
                List<CategoryMapping> parentCategoryMappings = currentCategory.getParentCategoryMappings();
                categoryMappingRepository.deleteAll(parentCategoryMappings);

//            set parents
                int parentCount = setParents(categoryRequest, category);

//                save edited category
                Category editedCategory = categoryRepository.save(category);
                CategoryResponse categoryResponse = modelMapper.map(editedCategory, CategoryResponse.class);

                result = new Result(0, "Success, with " + parentCount + " parent mapping(s)", categoryResponse);
            } else {
                result = new Result(1, "Error: Category not found");
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    private int setParents(CategoryRequest categoryRequest, Category category) {
//        find parent categories
        List<Integer> parentIds = categoryRequest.getParentIds();
        List<Category> parents = new ArrayList<>();
        int parentCount = 0;
        if (!parentIds.isEmpty()) {
            for (int parentId: parentIds
            ) {
//                check if parent exists. If it does, add it to parents
                Optional<Category> optionalParentCategory = categoryRepository.findById(parentId);
                if (optionalParentCategory.isPresent()) {
                    parents.add(optionalParentCategory.get());
                    parentCount ++;
                }
            }
        }
//        set parents to category
        category.setParents(parents);
        return parentCount;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(int id) {
        Result result;
        try {
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                List<Category> deletedCategories = stackDeletedCategories(category);
                List<CategoryMapping> deletedMappings = stackDeletedMappings(category);

                categoryRepository.deleteAll(deletedCategories);
                categoryMappingRepository.deleteAll(deletedMappings);
                result = new Result(0, "Success");
            } else {
                result = new Result(1, "Category not found");
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getAll(Pagination pagination) {
        Result result;
        try {
            PageRequest pageRequest = PageRequest.of(
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize(),
                    Sort.by(pagination.getDirection(), pagination.getSortBy())
            );
            List<Category> categories = categoryRepository.findAll(pageRequest).getContent();

            List<CategoryResponse> categoryResponses = new ArrayList<>();
            if (categories.isEmpty()) {
                result = new Result(0, "No categories fetched", categoryResponses);
            } else {
                categoryResponses = modelMapper.map(categories, new TypeToken<List<CategoryResponse>>() {}.getType());

                result = new Result(0, "Categories fetched", categoryResponses);
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
            Optional<Category> optionalCategory = categoryRepository.findById(id);
            if (optionalCategory.isPresent()) {
                Category category = optionalCategory.get();
                CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

                result = new Result(0, "Success", categoryResponse);
            } else {
                result = new Result(1, "Fail to retrieve data");
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getStackedRootCategories (int id) {
        Result result;
        try {
            List<Category> rootCategories = getRootCategories(id);
            if (!rootCategories.isEmpty()) {
                List<CategoryResponse> categoryResponses = modelMapper.map(rootCategories, new TypeToken<List<CategoryResponse>>() {}.getType());

                result = new Result(0, "Category trees acquired", categoryResponses);
            } else {
                result = new Result(1, "Fail to retrieve data");
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getParentCategories(int id) {
        Result result;
        try {
            List<Category> parentCategories = categoryRepository.findParents(id);
            if (parentCategories.isEmpty()) {
                result = new Result(0, "No parents found");
            } else {
                List<CategoryResponse> parentCategoryResponses = modelMapper.map(parentCategories, new TypeToken<List<CategoryResponse>>() {}.getType());
                result = new Result(0, "Parent categories acquired", parentCategoryResponses);
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result getChildCategories(int id) {
        Result result;
        try {
            List<Category> childCategories = categoryRepository.findCategory(id).getChildren();
            if (childCategories.isEmpty()) {
                result = new Result(0, "No children found");
            } else {
                List<CategoryResponse> childCategoryResponses = modelMapper.map(childCategories, new TypeToken<List<CategoryResponse>>() {}.getType());
                result = new Result(0, "Child categories acquired", childCategoryResponses);
            }
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
        }
        return result;
    }

    @Override
    public Result searchByName(String text, Pagination pagination) {
        Result result;
        try {
//            pageRequest
            log.info("pagination: page = {}, pageSize = {}, direction = {}, property = {}",
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize(),
                    pagination.getDirection(),
                    pagination.getSortBy()
            );
            PageRequest pageRequest = PageRequest.of(
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize(),
                    Sort.by(pagination.getDirection(), pagination.getSortBy())
            );

//            get search results (total quantity and list of categories)
            log.info("search text: {}", text);
            CategorySearch categorySearch = categoryExtendedRepository.searchByName(text, pageRequest);

            CategorySearchResponse categorySearchResponse = modelMapper.map(categorySearch, CategorySearchResponse.class);

            result = new Result(0,   categorySearchResponse.getTotalQuantity() + " results found", categorySearchResponse);
            log.info("Searching process completed without errors");
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
            log.error("Error: " + e.getMessage());
        }
        return result;
    }

    private List<Category> stackDeletedCategories(Category category) {
        List<Category> deletedCategories = new ArrayList<>();
        deletedCategories.add(category);

        List<Category> nextLevelChildren = category.getChildren();

//        browse all next level children's ids
        for (Category nextLevelChild: nextLevelChildren
             ) {
//            if a child category does NOT have another parent, delete it
            List<Category> parentsOfChild = nextLevelChild.getParents();
            if (parentsOfChild.isEmpty() || parentsOfChild.size() < 2) {
                deletedCategories.addAll(stackDeletedCategories(nextLevelChild));
            }
        }
        return deletedCategories;
    }

    private List<CategoryMapping> stackDeletedMappings(Category category) {
        List<CategoryMapping> deletedMappings = new ArrayList<>();

        List<Category> nextLevelChildren = category.getChildren();

        for (Category nextLevelChild: nextLevelChildren) {
            List<Category> parentsOfChild = nextLevelChild.getParents();
            if (parentsOfChild.isEmpty() || parentsOfChild.size() < 2) {
                deletedMappings.addAll(stackDeletedMappings(nextLevelChild));
            } else {
                List<CategoryMapping> parentMappingsOfChild = nextLevelChild.getChildCategoryMappings();
                for (CategoryMapping mapping: parentMappingsOfChild
                     ) {
                    if (mapping.getParent().getId() == category.getId()) {
                        deletedMappings.add(mapping);
                    }
                }
            }
        }
        return deletedMappings;
    }

    private List<Category> getRootCategories(int id) {
        List<Category> rootCategories = new ArrayList<>();

//        find category by passed id
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

//            if category has at least 1 parent mapping, continue browsing further ancestors
            List<Category> parents = category.getParents();
            if (!parents.isEmpty()) {
                for (Category parent: parents
                     ) {
//                    Recursion
                    rootCategories.addAll(getRootCategories(parent.getId()));
                }
//                if category does not have any parent, add it to rootCategories
            } else {
                rootCategories.add(category);
            }
        }
        return rootCategories;
    }
}
