package com.yashcode.EcommerceBackend.service.product;


import com.yashcode.EcommerceBackend.entity.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.entity.Product;
import com.yashcode.EcommerceBackend.entity.Products;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.CategoryClient.CategoryClient;
import com.yashcode.EcommerceBackend.service.ProductClient.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService implements IProductService {


    private final ModelMapper modelMapper;

    private final ProductClient pro;
    private final CategoryClient cate;

    @Override
    public Products addProduct(AddProductDTO addProductDTO) {
        if(productExists(addProductDTO.getName(),addProductDTO.getBrand()))
        {
            throw new AlreadyExistException(addProductDTO.getBrand()+" "+addProductDTO.getName()+" already exists");
        }
        Category category= Optional.ofNullable(cate.getCategoryByName(addProductDTO.getCategory().getName()))
                .orElseGet(()-> new Category(addProductDTO.getCategory().getName()));
        addProductDTO.setCategory(category);
        return pro.addProduct(addProductDTO);
    }

    private boolean productExists(String name,String brand){
        try {
            return pro.getProductByBrandAndName(name, brand) != null;
        }
        catch(Exception e){
            log.info("Product not found");
            throw new ResourceNotFoundException("Product not found with given name and brand!");
        }
    }

    private Product createProduct(AddProductDTO dto, Category category) {
        return new Product(
                dto.getName(),
                dto.getBrand(),
                dto.getPrice(),
                dto.getInventory(),
                dto.getDescription(),
                Arrays.asList(category)
        );
    }

    @Override
    public Products getProductById(Long id) {
        return pro.getProductById(id);
    }

//    @Override
//    public void deleteProductById(Long id) {
//        productRepository.findById(id).ifPresentOrElse(productRepository::delete,()->
//        {
//            log.info("Cannot delete the product");
//            throw new ProductNotFoundException("Product not found");
//        });
//    }
//
//    private Product updateExistingProducts(Product existingProduct, ProductUpdateDTO productUpdateDTO)
//    {
//        existingProduct.setName(productUpdateDTO.getName());
//        existingProduct.setPrice(productUpdateDTO.getPrice());
//        existingProduct.setBrand(productUpdateDTO.getBrand());
//        existingProduct.setDescription(productUpdateDTO.getDescription());
//        existingProduct.setInventory(productUpdateDTO.getInventory());
//        Category category=categoryRepository.findByName(productUpdateDTO.getCategory().getName());
//        if(category==null){
//            category=new Category(productUpdateDTO.getCategory().getName());
//            List<Category>categories=existingProduct.getCategory();
//            categories.add(category);
//            existingProduct.setCategory(categories);
//        }
//        return existingProduct;
//    }
//    @Override
//    public Product updateProduct(ProductUpdateDTO productUpdateDTO, Long productId) {
//        System.out.println(productUpdateDTO);
//        return productRepository.findById(productId)
//                .map(existingProduct->updateExistingProducts(existingProduct,productUpdateDTO))
//                .map(productRepository::save)
//                .orElseThrow(()->{
//                    log.warn("Product with given id is not found!");
//                    return new ProductNotFoundException("Product not Found");
//                });
//    }

    @Override
    public List<Products> getAllProducts() {
        try {
            List<Products> products = pro.getAllProducts();
            log.info("Successfully retrieved all products. Total products: {}", products.size());
            return products;
        } catch (ResourceNotFoundException e) {
            log.warn("There is no product present");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    @Override
    public List<Products> getProductsByBrandAndName(String brand, String name) {
        return pro.getProductByBrandAndName(brand,name);
    }

    @Override
    public Products getProductsByName(String name) {
        try {
            return pro.getProductName(name);
        }
        catch(ResourceNotFoundException e){
            log.info("Product with given name is not present");
            throw new ResourceNotFoundException("Product not found!");
        }
    }
    @Override
    public List<Products> getProductByCategoryAndBrandName(String category,String brandName) {
        try {
            return pro.getProductByCategoryBrandAndName(category,brandName);
        }
        catch (ResourceNotFoundException e){
            log.info("There is not product with the given category name!");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    @Override
    public List<Products>sortByField(String field){
        return pro.getProductsBySorting(field);
    }
    @Override
    public List<Products>sortByFieldDesc(String field){
        return pro.getProductsByDescSorting(field);
    }
    @Override
    public Page<Products> getProductByPagination(int offset, int pageSize){
        return pro.getProductsByPagination(offset,pageSize);
    }
    @Override
    public Page<Products> getProductByPaginationAndSorting(int offset, int pageSize,String field){
        return pro.getProductsByPaginationAndSorting(offset, pageSize, field);
    }
}




//
//    @Override
//    public List<Product> getProductsByBrand(String brand) {
//        try {
//            return productRepository.findByBrand(brand);
//        }
//        catch (ResourceNotFoundException e){
//            log.info("Product not found!");
//            throw new ResourceNotFoundException(e.getMessage());
//        }
//    }
//
//    @Override
//    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
//        try {
//            return productRepository.findByCategoryNameAndBrand(category, brand);
//        }
//        catch(ResourceNotFoundException e){
//            log.info("Product is not found with given brand name and category name");
//            throw new ResourceNotFoundException("Product with given category or brand name is not present");
//        }
//    }
//

//

//
//    @Override
//    public Long countProductsByBrandAndName(String brand, String name) {
//        return productRepository.countByBrandAndName(brand,name);
//    }
//    @Override
//    public ProductDto convertToDo(Product product){
//
//        ProductDto productDto=new ProductDto();
//        productDto.setId(product.getId());
//        productDto.setName(product.getName());
//
//        Category category = new Category();
//        category.setId(product.getCategory().get(0).getId());
//        category.setName(product.getCategory().get(0).getName());
//        productDto.setBrand(product.getBrand());
//        productDto.setPrice(product.getPrice());
//        productDto.setDescription(product.getDescription());
//        List<Image>images=imageRepository.findByProductId(product.getId());
//        List<ImageDto>dto=images.stream()
//                .map(image->modelMapper.map(image,ImageDto.class))
//                .toList();
//        productDto.setImages(dto);
//        return productDto;
//    }
//    @Override
//    public List<ProductDto>getConvertedProducts(List<Product>products) {
//        return products.stream().map(this::convertToDo).toList();
//    }




//}
