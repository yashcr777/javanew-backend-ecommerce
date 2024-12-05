package com.yashcode.EcommerceBackend.controller;


import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Products;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.category.ICategoryService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final ICategoryService categoryService;




    @GetMapping("/all")
    @RateLimiter(name="categoryRateLimiter",fallbackMethod = "categoryServiceFallBack")
    public ResponseEntity<ApiResponse> getCategories()
    {
        try {
            List<Category> categoryList=categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Found",categoryList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",null));
        }
    }
    public ResponseEntity<ApiResponse>categoryServiceFallBack(Exception e){

        log.info("Fallback is executed because service is down: ",e.getMessage());
        List<Category>categories=new ArrayList<>();
        Category ca=new Category();
        ca.setName("Dummy Category");
        ca.setProducts(null);
        ca.setId(1234L);
        categories.add(ca);
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",categories));
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>createCategory(@RequestBody Category c){
        try {
            Category category=categoryService.addCategory(c);
            return ResponseEntity.ok(new ApiResponse("Success",category));
        } catch (AlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/{id}/category")
    @RateLimiter(name="categoryRateLimiterForId",fallbackMethod = "categoryServiceFallBackForId")
    public ResponseEntity<ApiResponse>getCategoryById(@PathVariable Long id){
        try {
            Category category=categoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Found",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    public ResponseEntity<ApiResponse>categoryServiceFallBackForId(Long id,Exception e){
        Category ca=new Category();
        ca.setName("Dummy Category");
        ca.setProducts(null);
        ca.setId(1234L);
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",ca));
    }
    @GetMapping("/category/{name}/category")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String name){
        try {
            Category category=categoryService.getCategoryByName(name);
            if(category==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Category is not found",null));
            }
            return ResponseEntity.ok(new ApiResponse("Found by name",category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
//    @DeleteMapping("/category/{id}/delete")
//    public ResponseEntity<ApiResponse>deleteCategory(@PathVariable Long id){
//        try {
//            categoryService.deleteCategoryById(id);
//            return ResponseEntity.ok(new ApiResponse("Deleted Successfully",null));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
//        }
//    }
//    @PutMapping("/category/{id}/update")
//    public ResponseEntity<ApiResponse>updateCategory(@PathVariable Long id,@RequestBody Category category)
//    {
//        try {
//            Category updatedCategory=categoryService.updateCategory(category,id);
//            return ResponseEntity.ok(new ApiResponse("Updated Successfully",updatedCategory));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
//        }
//    }
    @GetMapping("/sort/{field}")
    public List<Category>sortCategory(@PathVariable String field){
        return categoryService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<Category>sortCategoryByDesc(@PathVariable String field){
        return categoryService.sortByFieldDesc(field);
    }
//    @GetMapping("/pagination/{offset}/{pageSize}")
//    public List<Category> categoryPagination(@PathVariable int offset, @PathVariable int pageSize){
//        return categoryService.getCategoryByPagination(offset,pageSize).getContent();
//    }
//    @GetMapping("/paginationAndSorting/{offset}/{pageSize}/{field}")
//    public List<Category> categoriesPaginationAndSorting(@PathVariable int offset, @PathVariable int pageSize,@PathVariable String field){
//        return categoryService.getCategoryByPaginationAndSorting(offset,pageSize,field).getContent();
//    }
}
