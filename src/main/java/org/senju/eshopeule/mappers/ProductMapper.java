package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.model.product.*;

import java.util.List;
import java.util.stream.Collectors;

//@Mapper(componentModel = "spring")
public interface ProductMapper<D extends ProductDTO> extends BaseMapper<Product, D> {

//    @Autowired
//    private ProductMetaService productMetaService;

//    @Override
//    public Product convertToEntity(ProductDTO dto) {
//        if (dto instanceof ProductDetailDTO) return convertToEntity((ProductDetailDTO) dto);
//        else return null;
//    }
//
//    @Override
//    public ProductDTO convertToDTO(Product entity) {
//        return null;
//    }

//    @Mapping(target = "isPublished", source = "published")
//    @Mapping(target = "isAllowedToOrder", source = "allowedToOrder")
//    @Mapping(target = "brand", expression = "java(mappingBrand(entity))")
//    @Mapping(target = "categories", expression = "java(mappingCategories(entity))")
//    @Mapping(target = "imageUrls", expression = "java(mappingImages(entity))")
//    @Mapping(target = "options", expression = "java(mappingOptions(entity))")
//    @Mapping(target = "productMeta", expression = "java(mappingProdMeta(entity))")
//    public abstract ProductDetailDTO convertToProductDetailDTO(Product entity);
//
//    @Mapping(target = "isPublished", ignore = true)
//    @Mapping(target = "gtin", ignore = true)
//    @Mapping(target = "sku", ignore = true)
//    @Mapping(target = "brand", expression = "java(mappingBrand(entity))")
//    @Mapping(target = "categories", ignore = true)
//    @Mapping(target = "isAllowedToOrder", source = "allowedToOrder")
//    @Mapping(target = "imageUrls", expression = "java(mappingImages(entity))")
//    @Mapping(target = "options", expression = "java(mappingOptions(entity))")
//    @Mapping(target = "productMeta", expression = "java(mappingProdMeta(entity))")
//    public abstract ProductDetailDTO toSimpleForm(Product entity);
//
//
//
//    @Mapping(target = "isPublished", source = "published")
//    @Mapping(target = "isAllowedToOrder", source = "allowedToOrder")
//    @Mapping(target = "brand", expression = "java(mappingBrand(dto))")
//    @Mapping(target = "productCategories", expression = "java(mappingCategories(dto))")
//    @Mapping(target = "productImages", ignore = true)
//    @Mapping(target = "productOptions", ignore = true)
//    public abstract Product convertToEntity(ProductDetailDTO dto);
//
//
//    public BrandDTO mappingBrand(Product entity) {
//        return BrandDTO.builder()
//                .id(entity.getBrand().getId())
//                .name(entity.getBrand().getName())
//                .slug(entity.getBrand().getSlug())
//                .build();
//    }
//
//
//    public Brand mappingBrand(ProductDetailDTO dto) {
//        return Brand.builder()
//                .id(dto.getBrand().getId())
//                .name(dto.getBrand().getName())
//                .slug(dto.getBrand().getSlug())
//                .build();
//    }
//
//    public List<CategoryDTO> mappingCategories(Product entity) {
//        return entity.getProductCategories().stream()
//                .map(pc -> CategoryDTO.builder()
//                        .id(pc.getCategory().getId())
//                        .name(pc.getCategory().getName())
//                        .slug(pc.getCategory().getSlug())
//                        .build())
//                .toList();
//    }
//
//    public List<ProductCategory> mappingCategories(ProductDetailDTO dto) {
//        return dto.getCategories().stream()
//                .map(c -> ProductCategory.builder()
//                        .category(new Category(c.getId(), c.getName()))
//                        .build())
//                .collect(Collectors.toList());
//    }
//
//    public List<String> mappingImages(Product entity) {
//        return entity.getProductImages().stream()
//                .map(ProductImage::getImageUrl)
//                .toList();
//    }
//
//    public List<ProductOptionDTO> mappingOptions(Product entity) {
//        if (!entity.isHasOptions()) return null;
//        return entity.getProductOptions().stream()
//                .map(op -> ProductOptionDTO.builder()
//                        .id(op.getId())
//                        .name(op.getName())
//                        .attributes(Mappers.getMapper(ProductOptionMapper.class).mappingAttributes(op))
//                        .build())
//                .toList();
//    }
//
//    public ProductMetaDTO mappingProdMeta(Product entity) {
//        try {
//            return productMetaService.getByProductId(entity.getId());
//        } catch (NotFoundException ex) {
//            return null;
//        }
//    }
}
