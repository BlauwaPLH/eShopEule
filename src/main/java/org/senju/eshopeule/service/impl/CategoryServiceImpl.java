package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.mappers.CategoryMapper;
import org.senju.eshopeule.repository.jpa.CategoryRepository;
import org.senju.eshopeule.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.CategoryExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Override
    public void createNewCategory(CategoryDTO dto) throws ObjectAlreadyExistsException {
        if (categoryRepository.checkCategoryExistsWithNameOrSlug(dto.getName(), dto.getSlug())) {
            throw new ObjectAlreadyExistsException(CATEGORY_ALREADY_EXITS_MSG);
        }
        categoryRepository.save(categoryMapper.convertToEntity(dto));
    }

    @Override
    @Cacheable(cacheNames = "categoryCache", key = "#id")
    public CategoryDTO getById(String id) throws NotFoundException {
        return categoryMapper.convertToDTO(categoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(CATEGORY_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public List<CategoryDTO> getAllCategory() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = "categoryCache", key = "#id")
    public void deleteById(String id) {
        categoryRepository.deleteById(id);
    }
}
