package com.yashcode.EcommerceBackend.service.product;


import com.yashcode.EcommerceBackend.entity.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.Products;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Products addProduct(AddProductDTO addProductDTO);
    Products getProductById(Long id);
//    void deleteProductById(Long id);
//    Product updateProduct(ProductUpdateDTO product, Long productId);
    List<Products>getAllProducts();
//    List<Product>getProductByCategory(String category);

    List<Products> getProductByCategoryAndBrandName(String category,String brandName);
    Products getProductsByName(String name);
    List<Products>getProductsByBrandAndName(String brand,String name);
//    Long countProductsByBrandAndName(String brand,String name);
//    ProductDto convertToDo(Product product);
//    List<ProductDto>getConvertedProducts(List<Product>products);
    List<Products>sortByField(String field);
    List<Products>sortByFieldDesc(String field);
    Page<Products> getProductByPagination(int offset, int pageSize);
    Page<Products> getProductByPaginationAndSorting(int offset, int pageSize,String field);

}
