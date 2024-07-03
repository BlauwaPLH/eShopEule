package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;

import java.util.List;

public interface CategoryService {

    void createNewCategory(CategoryDTO dto) throws ObjectAlreadyExistsException;

    CategoryDTO getById(String id) throws NotFoundException;

    List<CategoryDTO> getAllCategory();

    void deleteById(String id);
}
