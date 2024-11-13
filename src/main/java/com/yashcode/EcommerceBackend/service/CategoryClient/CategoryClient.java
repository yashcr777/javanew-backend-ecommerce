package com.yashcode.EcommerceBackend.service.CategoryClient;


import com.yashcode.EcommerceBackend.entity.Category;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(url="http://localhost:8082",value="Category-Client")
public interface CategoryClient {
    @GetMapping("/api/v1/categories/category/{name}/categoryByName")
    Category getCategoryByName(@PathVariable String name);
    @GetMapping("/api/v1/categories/all")
    List<Category> getAllCategories();
    @PostMapping("/api/v1/categories/add")
    Category addCategory(Category category);
}
