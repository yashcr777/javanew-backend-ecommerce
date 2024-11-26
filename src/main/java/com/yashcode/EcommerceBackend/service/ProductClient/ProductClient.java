package com.yashcode.EcommerceBackend.service.ProductClient;



import com.yashcode.EcommerceBackend.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.Products;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="PRODUCT-MICROSERVICE")
public interface ProductClient {

    @GetMapping("/api/v1/products/all")
    List<Products>getAllProducts();
    @GetMapping("/api/v1/products/product/{productId}/product")
    Products getProductById(@PathVariable Long productId);
    @GetMapping("/api/v1/products/products/by/brand-and-name")
    List<Products>getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName);
    @PostMapping("/api/v1/products/add")
    Products addProduct(@RequestBody AddProductDTO addProductDTO);
    @GetMapping("/api/v1/products/products/by/name")
    Products getProductName(@RequestParam String name);
    @GetMapping("/api/v1/products/products/by/category-and-name")
    List<Products>getProductByCategoryBrandAndName(@RequestParam String category,@RequestParam String brandName);
    @GetMapping("/api/v1/products/products/by/brand-name")
    List<Products>getProductByBrandName(@RequestParam String brandName);
    @GetMapping("/api/v1/products/sort/{field}")
    List<Products>getProductsBySorting(@PathVariable String field);
    @GetMapping("api/v1/products/sortdesc/{field}")
    List<Products>getProductsByDescSorting(@PathVariable String field);
}