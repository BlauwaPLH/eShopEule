package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.model.product.Category;


@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryDTO> {

    @Override
    @Mapping(target = "createdOn", expression = "java(entity.getCreatedOn())")
    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy())")
    @Mapping(target = "lastModifiedOn", expression = "java(entity.getLastModifiedOn())")
    @Mapping(target = "lastModifiedBy", expression = "java(entity.getLastModifiedBy())")
    @Mapping(target = "parent", expression = "java(mappingParent(entity))")
    @Mapping(target = "isPublished", source = "published")
    CategoryDTO convertToDTO(Category entity);

    @Override
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "parent", expression = "java(mappingParent(dto))")
    @Mapping(target = "isPublished", source = "published")
    Category convertToEntity(CategoryDTO dto);

    default Category updateFromDTO(CategoryDTO dto, Category loadedCategory) {
        if (dto.getName() != null) loadedCategory.setName(dto.getName());
        if (dto.getSlug() != null) loadedCategory.setSlug(dto.getSlug());
        if (dto.getDescription() != null) loadedCategory.setDescription(dto.getDescription());
        if (dto.getMetaDescription() != null) loadedCategory.setDescription(dto.getMetaDescription());
        if (dto.getMetaKeyword() != null) loadedCategory.setMetaKeyword(dto.getMetaKeyword());

        loadedCategory.setPublished(dto.isPublished());
        loadedCategory.setParent(mappingParent(dto));

        return loadedCategory;
    }

    default CategoryDTO mappingParent(Category entity) {
        if (entity.getParent() == null) return null;
        return CategoryDTO.builder()
                .id(entity.getParent().getId())
                .name(entity.getParent().getName())
                .slug(entity.getParent().getSlug())
                .build();
    }

    default Category mappingParent(CategoryDTO dto) {
        if (dto.getParent() == null) return null;
        return Category.builder()
                .id(dto.getParent().getId())
                .name(dto.getParent().getName())
                .slug(dto.getParent().getSlug())
                .build();
    }
}
