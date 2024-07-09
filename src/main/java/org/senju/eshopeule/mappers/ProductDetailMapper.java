package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.model.product.*;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ProductDetailMapper implements ProductMapper<ProductDetailDTO> {

    @Autowired
    private ProductMetaRepository productMetaRepository;

    @Override
    public final Product convertToEntity(ProductDetailDTO dto) {return null;}

    @Override
    @Mapping(target = "brand", expression = "java(mappingBrand(entity))")
    @Mapping(target = "categories", expression = "java(mappingCategories(entity))")
    @Mapping(target = "imageUrls", expression = "java(mappingImageUrls(entity))")
    @Mapping(target = "options", expression = "java(mappingOptions(entity))")
    @Mapping(target = "productMeta", expression = "java(mappingProductMeta(entity))")
    @Mapping(target = "createdOn", expression = "java(entity.getCreatedOn())")
    @Mapping(target = "createdBy", expression = "java(entity.getCreatedBy())")
    @Mapping(target = "lastModifiedOn", expression = "java(entity.getLastModifiedOn())")
    @Mapping(target = "lastModifiedBy", expression = "java(entity.getLastModifiedBy())")
    public abstract ProductDetailDTO convertToDTO(Product entity);

    protected BrandDTO mappingBrand(Product entity) {
        final Brand brandEntity = entity.getBrand();
        return BrandDTO.builder()
                .id(brandEntity.getId())
                .name(brandEntity.getName())
                .slug(brandEntity.getSlug())
                .build();
    }

    protected List<CategoryDTO> mappingCategories(Product entity) {
        return entity.getProductCategories()
                .stream()
                .map(pc -> {
                    final Category category = pc.getCategory();
                    return CategoryDTO.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .slug(category.getSlug())
                            .build();
                })
                .toList();
    }

    protected List<ProductOptionDTO> mappingOptions(Product entity) {
        return entity.getProductOptions()
                .stream()
                .map(Mappers.getMapper(ProductOptionMapper.class)::convertToDTO)
                .toList();
    }

    protected ProductMetaDTO mappingProductMeta(Product entity) {
        final var prodMeta = productMetaRepository.findByProductId(entity.getId()).orElse(null);
        if (prodMeta == null) return null;
        return Mappers.getMapper(ProductMetaMapper.class).convertToDTO(prodMeta);
    }
}
