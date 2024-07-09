package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.exceptions.NotFoundException;

import java.util.List;

public interface CategoryService {

    void createNewCategory(CategoryDTO dto) ;

    CategoryDTO updateCategory(CategoryDTO dto) ;

    CategoryDTO getById(String id) throws NotFoundException;

    List<CategoryDTO> getAllCategories();

    List<CategoryDTO> getAllCategoryChildren(String parentId);

    void deleteById(String id);
}
