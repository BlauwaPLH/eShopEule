package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.model.product.Category;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryDTO> {

    @Override
    @Mapping(target = "createdOn", expression = "java(entity.getCreatedOn())")
    @Mapping(target = "createdBy", expression = "java(entity.getCreatedOn())")
    @Mapping(target = "lastModifiedOn", expression = "java(entity.getLastModifiedOn())")
    @Mapping(target = "lastModifiedBy", expression = "java(entity.getLastModifiedBy())")
    @Mapping(target = "parent", expression = "java(mappingParent(entity))")
    @Mapping(target = "children", expression = "java(mappingChildren(entity))")
    CategoryDTO convertToDTO(Category entity);

    @Override
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "parent", expression = "java(mappingParent(dto))")
    @Mapping(target = "children", expression = "java(mappingChildren(dto))")
    Category convertToEntity(CategoryDTO dto);

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
                .id(dto.getId())
                .name(dto.getName())
                .slug(dto.getSlug())
                .build();
    }

    default List<CategoryDTO> mappingChildren(Category entity) {
        if (entity.getChildren() == null) return null;
        return entity.getChildren().stream()
                .map(c -> CategoryDTO.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .build()
                )
                .collect(Collectors.toList());
    }

    default List<Category> mappingChildren(CategoryDTO dto) {
        if (dto.getChildren() == null) return null;
        return dto.getChildren().stream()
                .map(c -> Category.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .slug(c.getSlug())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
