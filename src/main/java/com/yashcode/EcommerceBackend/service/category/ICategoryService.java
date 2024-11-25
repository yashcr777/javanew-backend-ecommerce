package com.yashcode.EcommerceBackend.service.category;

import com.yashcode.EcommerceBackend.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    List<Category>sortByFieldDesc(String field);
    List<Category>sortByField(String field);
    Category getCategoryById(Long id);
}
