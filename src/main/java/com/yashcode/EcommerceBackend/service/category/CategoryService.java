package com.yashcode.EcommerceBackend.service.category;

import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.CategoryClient.CategoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryClient cate;

//    @Override
//    public Category getCategoryById(Long id) {
//        return categoryRepository.findById(id).orElseThrow(()->{
//            log.error("Category not found with given id");
//            return new CategoryNotFoundException("Category Not Found");});
//    }
//
    @Override
    public Category getCategoryByName(String name) {
        try {
            return cate.getCategoryByName(name);
        }
        catch(Exception e){
            log.error("Category not found!");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
//
//    @Override
//    public List<Category> getAllCategories() {
//        try{
//            log.info("List of categories is returned");
//            return categoryRepository.findAll();
//        }
//        catch(Exception e){
//            log.error("There is no categories");
//            throw new ResourceNotFoundException("There is no categories");
//        }
//
//    }
//
//    @Override
//    public Category addCategory(Category category) {
//        return Optional.of(category).filter(c->!categoryRepository.existsByName(c.getName()))
//                .map(categoryRepository::save)
//                .orElseThrow(()->{
//                    log.error("Category already exists");
//                    return new AlreadyExistException(category.getName()+" Already exist");
//                });
//    }
//
//    @Override
//    public Category updateCategory(Category category, Long id) {
//        return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
//            oldCategory.setName(category.getName());
//            log.info("Category Updated Successfully");
//            return categoryRepository.save(oldCategory);
//        }).orElseThrow(()->{
//            log.error("Category Updation failed");
//            return new CategoryNotFoundException("Category not Found");
//        });
//    }
//
//
//
//    @Override
//    public void deleteCategoryById(Long id) {
//        categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete,()->{
//            log.error("Deletion with given id not possible");
//            throw new CategoryNotFoundException("Category Not Found!");
//        });
//    }
//
//    @Override
//    public List<Category>sortByField(String field){
//        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC,field));
//    }
//    @Override
//    public List<Category>sortByFieldDesc(String field){
//        return categoryRepository.findAll((Sort.by(Sort.Direction.DESC,field)));
//    }
//    @Override
//    public Page<Category> getCategoryByPagination(int offset, int pageSize){
//        return categoryRepository.findAll(PageRequest.of(offset,pageSize));
//    }
//    @Override
//    public Page<Category> getCategoryByPaginationAndSorting(int offset, int pageSize,String field){
//        return categoryRepository.findAll(PageRequest.of(offset,pageSize).withSort(Sort.by(Sort.Direction.DESC,field)));
//    }
}
