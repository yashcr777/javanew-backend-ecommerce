package com.yashcode.EcommerceBackend.service.category;

import com.yashcode.EcommerceBackend.entity.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
}
