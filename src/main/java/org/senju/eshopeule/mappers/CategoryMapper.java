package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.CategoryDTO;
import org.senju.eshopeule.dto.ProductAttributeDTO;
import org.senju.eshopeule.model.product.Category;
import org.senju.eshopeule.model.product.ProductAttribute;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper<Category, CategoryDTO> {

    @Override
    @Mapping(target = "parent", expression = "java(mappingParent(entity))")
    @Mapping(target = "productAttributes", expression = "java(mappingProdAttr(entity))")
    @Mapping(target = "isPublished", source = "published")
    CategoryDTO convertToDTO(Category entity);

    @Override
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "parent", expression = "java(mappingParent(dto))")
    @Mapping(target = "productAttributes", expression = "java(mappingProdAttr(dto))")
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
        loadedCategory.setProductAttributes(mappingProdAttr(dto));
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

    default List<ProductAttributeDTO> mappingProdAttr(Category entity) {
        if (entity.getProductAttributes() == null) return null;
        return entity.getProductAttributes().stream()
                .map(s -> Mappers.getMapper(ProductAttributeMapper.class).convertToDTO(s))
                .toList();
    }

    default List<ProductAttribute> mappingProdAttr(CategoryDTO dto) {
        if (dto.getProductAttributes() == null) return null;
        return dto.getProductAttributes().stream()
                .map(s -> Mappers.getMapper(ProductAttributeMapper.class).convertToEntity(s))
                .collect(Collectors.toList());
    }
}
