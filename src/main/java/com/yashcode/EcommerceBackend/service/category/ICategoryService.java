package com.yashcode.EcommerceBackend.service.category;

import com.yashcode.EcommerceBackend.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICategoryService {
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    List<Category>sortByFieldDesc(String field);
    List<Category>sortByField(String field);
    Category getCategoryById(Long id);
    Page<Category> getCategoryByPagination(int offset, int pageSize);
    Page<Category> getCategoryByPaginationAndSorting(int offset, int pageSize,String field);
}
