package com.yashcode.EcommerceBackend.service.Product;


import com.yashcode.EcommerceBackend.entity.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.Products;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    Products addProduct(AddProductDTO addProductDTO);
    Products getProductById(Long id);
    List<Products>getAllProducts();
    List<Products> getProductByCategoryAndBrandName(String category,String brandName);
    Products getProductsByName(String name);
    List<Products>getProductsByBrandAndName(String brand,String name);
    List<Products>sortByField(String field);
    List<Products>sortByFieldDesc(String field);
    Page<Products> getProductByPagination(int offset, int pageSize);
    Page<Products> getProductByPaginationAndSorting(int offset, int pageSize,String field);

}
