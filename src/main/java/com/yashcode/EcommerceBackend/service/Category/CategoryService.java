package com.yashcode.EcommerceBackend.service.Category;

import com.yashcode.EcommerceBackend.entity.Category;
import com.yashcode.EcommerceBackend.exceptions.AlreadyExistException;
import com.yashcode.EcommerceBackend.exceptions.ResourceNotFoundException;
import com.yashcode.EcommerceBackend.service.CategoryClient.CategoryClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryClient cate;

    @Override
    public Category getCategoryById(Long id) {
        return cate.getCategoryById(id);
    }

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
    @Override
    public List<Category> getAllCategories() {
        try{
            log.info("List of categories is returned");
            return cate.getAllCategories();
        }
        catch(Exception e){
            log.error("There is no categories");
            throw new ResourceNotFoundException("There is no categories");
        }

    }

    @Override
    public Category addCategory(Category category) {
        if(cate.getCategoryByName(category.getName())!=null){
            log.error("Category with given name already exists");
            throw new AlreadyExistException(category.getName()+" already exists");
        }
        return cate.addCategory(category);
    }

    @Override
    public List<Category>sortByField(String field){
        return cate.getCategoriesBySorting(field);
    }
    @Override
    public List<Category>sortByFieldDesc(String field){
        return cate.getCategoriesByDescSorting(field);
    }


    @Override
    public Page<Category> getCategoryByPagination(int offset, int pageSize){
        return cate.getCategoryByPagination(offset,pageSize);
    }
    @Override
    public Page<Category> getCategoryByPaginationAndSorting(int offset, int pageSize,String field){
        return cate.getCategoryByPaginationAndField(offset,pageSize,field);
    }
}
