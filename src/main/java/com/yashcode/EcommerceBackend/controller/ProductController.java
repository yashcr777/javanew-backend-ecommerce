package com.yashcode.EcommerceBackend.controller;
import com.yashcode.EcommerceBackend.entity.dto.AddProductDTO;
import com.yashcode.EcommerceBackend.entity.Products;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.response.ApiResponse;
import com.yashcode.EcommerceBackend.service.ProductClient.ProductClient;
import com.yashcode.EcommerceBackend.service.Product.IProductService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import static org.springframework.http.HttpStatus.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final ProductClient client;


    int retryCount=1;


    @GetMapping("/all")
//    @CircuitBreaker(name="productBreaker",fallbackMethod = "productServiceFallBack")
//    @Retry(name="productService",fallbackMethod = "productServiceFallBack")
    @RateLimiter(name="productRateLimiter",fallbackMethod = "productServiceFallBack")
    public ResponseEntity<ApiResponse>getAllProducts(){
        log.info("Retry count: {}",retryCount);
        retryCount++;
        try {
            List<Products>productList=productService.getAllProducts();
            return ResponseEntity.ok(new ApiResponse("Found All Products",productList));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error",null));
        }
    }

    public ResponseEntity<ApiResponse>productServiceFallBack(Exception e){

        List<Products>products=new ArrayList<>();
        Products p=new Products();
        p.setId(1234L);
        p.setBrand("Dummy T");
        p.setName("Dummy");
        p.setDescription("This is dummy product because service is down");
        p.setInventory(0);
        products.add(p);
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",products));
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse>addProduct(@RequestBody AddProductDTO addProductDTO)
    {
        try {
            Products product=productService.addProduct(addProductDTO);
            return ResponseEntity.ok(new ApiResponse("success",product));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/product/{productId}/product")
    @RateLimiter(name="productRateLimiterForId",fallbackMethod = "productServiceFallBackForId")
    public ResponseEntity<ApiResponse>getProductById(@PathVariable Long productId)
    {
        try {
            Products product=productService.getProductById(productId);

            return ResponseEntity.ok(new ApiResponse("success",product));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
    public ResponseEntity<ApiResponse>productServiceFallBackForId(Long productId,Exception e){
        Products p=new Products();
        p.setId(1234L);
        p.setBrand("Dummy T");
        p.setName("Dummy");
        p.setDescription("This is dummy product because service is down");
        p.setInventory(0);
        return ResponseEntity.status(TOO_MANY_REQUESTS).body(new ApiResponse("Service is down",p));
    }

    @GetMapping("/products/by/brand-and-name")
    public ResponseEntity<ApiResponse>getProductsByBrandAndName(@RequestParam String brandName,@RequestParam String productName)
    {
        try {
            List<Products>products=productService.getProductsByBrandAndName(brandName,productName);
            if(products==null)
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with this brand name and product name",null));
            }
            return ResponseEntity.ok(new ApiResponse("success",products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/category-and-name")
    public ResponseEntity<ApiResponse>getProductsByCategoryAndBrand(@RequestParam String category,@RequestParam String brandName)
    {
        try {
            List<Products>products=productService.getProductByCategoryAndBrandName(category,brandName);
            if(products==null)
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given category name and brand name",null));
            }

            return ResponseEntity.ok(new ApiResponse("success",products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/name")
    public ResponseEntity<ApiResponse>getProductsByName(@RequestParam String name)
    {
        try {
            Products products=productService.getProductsByName(name);
            if(products==null)
            {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given name",null));
            }

            return ResponseEntity.ok(new ApiResponse("Success",products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
    @GetMapping("/products/by/brand-name")
    public ResponseEntity<ApiResponse>getProductsByBrandName(@RequestParam String brandName)
    {
        try {
            List<Products>products=client.getProductByBrandName(brandName);
            if(products.isEmpty()){
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products with given brand name",null));
            }
            return ResponseEntity.ok(new ApiResponse("success",products));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
        }
    }
//    @GetMapping("/product/count/by-brand/and-name")
//    public ResponseEntity<ApiResponse>countProductByBrandNameAndName(@RequestParam String brand,@RequestParam String name)
//    {
//        try {
//            var productCount=productService.countProductsByBrandAndName(brand,name);
//            return ResponseEntity.ok(new ApiResponse("Product Count@",productCount));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.ok(new ApiResponse(e.getMessage(),null));
//        }
//    }
    @GetMapping("/sort/{field}")
    public List<Products>sortProducts(@PathVariable String field){
        return productService.sortByField(field);
    }
    @GetMapping("/sortdesc/{field}")
    public List<Products>sortProductsByDesc(@PathVariable String field){
        return productService.sortByFieldDesc(field);
    }
    @GetMapping("/pagination")
    public List<Products> productPagination(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize){
        return productService.getProductByPagination(offset,pageSize).getContent();
    }
    @GetMapping("/paginationAndSorting/{field}")
    public List<Products> productPaginationAndSorting(@RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10")  int pageSize,@PathVariable String field){
        return productService.getProductByPaginationAndSorting(offset,pageSize,field).getContent();
    }
}





































//    @PutMapping("/product/{productId}/update")
//    public ResponseEntity<ApiResponse>updateProduct(@RequestBody ProductUpdateDTO updateDTO,@PathVariable Long productId)
//    {
//        try {
//            Product prod=productService.updateProduct(updateDTO,productId);
//            ProductDto dto=productService.convertToDo(product);
//            return ResponseEntity.ok(new ApiResponse("success",dto));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
//        }
//    }
//
//    @DeleteMapping("/product/{productId}/delete")
//    public ResponseEntity<ApiResponse>deleteProduct(@PathVariable Long productId)
//    {
//        try {
//            productService.deleteProductById(productId);
//            return ResponseEntity.ok(new ApiResponse("success",productId));
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
//        }
//    }
