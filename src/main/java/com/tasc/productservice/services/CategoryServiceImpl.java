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
import org.springframework.data.domain.Page;
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
//            save new category
            Category category = modelMapper.map(categoryRequest, Category.class);
            Category newCategory = categoryRepository.save(category);
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

//            Add new parent mappings
            List<Integer> parentIds = categoryRequest.getParentIds();
            int parentCount = 0;
            if (!parentIds.isEmpty()) {
                int childId = newCategory.getId();
                for (int parentId: parentIds
                ) {
                    if (saveNewMapping(parentId, childId)) {
                        parentCount ++;
                    }
                }
            }
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
//            save edited category
            Category category = modelMapper.map(categoryRequest, Category.class);
            category.setId(id);
            Category editedCategory = categoryRepository.save(category);
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

//            Delete all existing parent mappings
            List<CategoryMapping> parentCategoryMappings = (List<CategoryMapping>) editedCategory.getParentCategoryMappings();
            categoryMappingRepository.deleteAll(parentCategoryMappings);

//            Add new parent mappings
            List<Integer> parentIds = categoryRequest.getParentIds();
            int parentCount = 0;
            if (!parentIds.isEmpty()) {
                for (int parentId: parentIds
                ) {
                    if (saveNewMapping(parentId, id)) {
                        parentCount ++;
                    }
                }
            }
            result = new Result(0, "Success, with " + parentCount + " parent mapping(s)", categoryResponse);
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
    public Result getAll(Pagination pagination) {
        Result result;
        try {
            PageRequest pageRequest = PageRequest.of(
                    pagination.getPageNumber() - 1,
                    pagination.getPageSize(),
                    Sort.by(pagination.getDirection(), pagination.getSortBy())
            );
            Page<Category> categories = categoryRepository.findAll(pageRequest);

            List<CategoryResponse> categoryResponses = new ArrayList<>();
            if (categories.isEmpty()) {
                result = new Result(0, "No categories fetched", categoryResponses);
            } else {
                for (Category category: categories
                ) {
                    CategoryResponse categoryResponse = stackCategories(category.getId());
                    categoryResponses.add(categoryResponse);
                }
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
            CategoryResponse categoryResponse = stackCategories(id);
            if (categoryResponse != null) {
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
    public Result getStackedRootCategories (int id) {
        Result result;
        try {
            List<Category> rootCategories = getRootCategory(id);
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            for (Category root: rootCategories
                 ) {
                categoryResponses.add(stackCategories(root.getId()));
            }
            result = new Result(0, "Category trees acquired", categoryResponses);
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
            List<Category> childCategories = categoryRepository.findChildren(id);
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

//            convert categories from categorySearch to a list of CategoryResponse
            List<CategoryResponse> categoryResponses = new ArrayList<>();
            for (Category category: categorySearch.getCategories()
            ) {
                int id = category.getId();
//                stack result categories and their children
                CategoryResponse categoryResponse = stackCategories(id);

//                find parents, map to CategoryResponse and set to each categoryResponse
                if (categoryResponse != null) {
                    List<Category> parents = categoryRepository.findParents(id);
                    List<CategoryResponse> parentResponses = modelMapper.map(parents, new TypeToken<List<CategoryResponse>>() {}.getType());
                    categoryResponse.setParents(parentResponses);
                }

//                add to categoryResponses
                categoryResponses.add(categoryResponse);
            }
//            set an instance of CategorySearchResponse, which consists of total quantity and categoryResponses, to result.data
            long totalQuantity = categorySearch.getTotalQuantity();
            CategorySearchResponse categorySearchResponse = new CategorySearchResponse(totalQuantity, categoryResponses);

            result = new Result(0,   totalQuantity + " results found", categorySearchResponse);
            log.info("Searching process completed without errors");
        } catch (Exception e) {
            result = new Result(1, e.getMessage());
            log.error("Error: " + e.getMessage());
        }
        return result;
    }

    private CategoryResponse stackCategories(int id) {
//        if category with passed id exists, map it to response DTO
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);

            List<CategoryResponse> childResponses = new ArrayList<>();
//            get child mappings that associate with category
            List<CategoryMapping> childMappings = (List<CategoryMapping>) category.getChildCategoryMappings();
            if (!childMappings.isEmpty()) {
                for (CategoryMapping childMapping: childMappings
                ) {
//                    Recursion. Call function itself, pass children's ids as parameters, add returned result to childResponses
                    childResponses.add(stackCategories(childMapping.getChild().getId()));
                }
            }
//            set stacked child categories to categoryResponse's children attribute
            categoryResponse.setChildren(childResponses);
            return categoryResponse;
        } else {
            return null;
        }
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
            if (mapping.getParent().getId() == parentId) {
                childIds.add(mapping.getChild().getId());
            }
        }
        return childIds;
    }

    private boolean checkMultiParentCategory(int id, List<CategoryMapping> categoryMappings) {
        int parentCount = 0;
        for (CategoryMapping mapping: categoryMappings
             ) {
            if (mapping.getChild().getId() == id) {
                parentCount ++;
            }
        }
        return parentCount >= 2;
    }

    private CategoryMapping getMappingByChildAndParent(int childId, int parentId, List<CategoryMapping> categoryMappings) {
        CategoryMapping categoryMapping = new CategoryMapping();
        for (CategoryMapping mapping: categoryMappings
            ) {
            if (mapping.getChild().getId() == childId && mapping.getParent().getId() == parentId) {
                categoryMapping = mapping;
            }
        }
        return categoryMapping;
    }

    private List<Category> getRootCategory(int id) {
        List<Category> rootCategories = new ArrayList<>();

//        find category by passed id
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();

//            if category has at least 1 parent mapping, continue browsing further ancestors
            List<CategoryMapping> parentMappings = (List<CategoryMapping>) category.getParentCategoryMappings();
            if (!parentMappings.isEmpty()) {
                for (CategoryMapping parentMapping: parentMappings
                     ) {
//                    Recursion
                    rootCategories.addAll(getRootCategory(parentMapping.getParent().getId()));
                }
//                if category does not have any parent, add it to rootCategories
            } else {
                rootCategories.add(category);
            }
        }
        return rootCategories;
    }

    private boolean saveNewMapping(int parentId, int childId) {
//          check whether child and parent categories exist. If they both do, create a new mapping that consists of them
        Optional<Category> optionalParentCategory = categoryRepository.findById(parentId);
        Optional<Category> optionalNewCategory = categoryRepository.findById(childId);
        if (parentId != childId &&
            optionalParentCategory.isPresent() &&
            optionalNewCategory.isPresent()
        ) {
            CategoryMapping categoryMapping = new CategoryMapping(
                    optionalParentCategory.get(),
                    optionalNewCategory.get()
            );
            categoryMappingRepository.save(categoryMapping);
            return true;
        } else {
            return false;
        }
    }
}
