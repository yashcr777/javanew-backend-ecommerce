package com.yashcode.EcommerceBackend.service.CategoryClient;


import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Products;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="PRODUCT-MICROSERVICE")
public interface CategoryClient {
    @GetMapping("/api/v1/categories/category/{name}/categoryByName")
    Category getCategoryByName(@PathVariable String name);
    @GetMapping("/api/v1/categories/all")
    List<Category> getAllCategories();
    @PostMapping("/api/v1/categories/add")
    Category addCategory(Category category);
    @GetMapping("/api/v1/categories/sort/{field}")
    List<Category>getCategoriesBySorting(@PathVariable String field);
    @GetMapping("api/v1/categories/sortdesc/{field}")
    List<Category>getCategoriesByDescSorting(@PathVariable String field);
    @GetMapping("api/v1/categories/category/{id}/category")
    Category getCategoryById(@PathVariable Long id);
    @GetMapping("api/v1/categories/pagination")
    Page<Category> getCategoryByPagination(@RequestParam int offset, @RequestParam int pageSize);
    @GetMapping("api/v1/categories/paginationAndSorting/{field}")
    Page<Category> getCategoryByPaginationAndField(@RequestParam int offset,@RequestParam int pageSize,@PathVariable String field);
}
