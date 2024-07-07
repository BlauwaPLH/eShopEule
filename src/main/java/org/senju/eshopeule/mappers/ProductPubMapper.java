package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.ProductAttributeValueDTO;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.dto.ProductPubDTO;
import org.senju.eshopeule.model.product.*;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductPubMapper implements ProductMapper<ProductPubDTO> {

    @Autowired
    private ProductMetaRepository productMetaRepository;

    @Override
    public final Product convertToEntity(ProductPubDTO dto) {return null;}

    @Override
    @Mapping(target = "brand", expression = "java(mappingBrand(entity))")
    @Mapping(target = "categories", expression = "java(mappingCategories(entity))")
    @Mapping(target = "imageUrls", expression = "java(mappingImageUrls(entity))")
    @Mapping(target = "options", expression = "java(mappingOptions(entity))")
    @Mapping(target = "productMeta", expression = "java(mappingProductMeta(entity))")
    public abstract ProductPubDTO convertToDTO(Product entity);

    protected String mappingBrand(Product entity) {
        return entity.getBrand().getName();
    }

    protected List<String> mappingCategories(Product entity) {
        return entity.getProductCategories().stream()
                .map(pc -> pc.getCategory().getName())
                .toList();
    }

    protected List<ProductOptionDTO> mappingOptions(Product entity) {
        if (!entity.getHasOptions()) return null;
        return entity.getProductOptions()
                .stream()
                .map(po -> ProductOptionDTO.builder()
                        .attributes(mappingProdAttrVal(po))
                        .build())
                .toList();
    }

    protected Map<String, ProductAttributeValueDTO> mappingProdAttrVal(ProductOption option) {
        return option.getProductAttributeValues()
                .stream()
                .collect(Collectors.toMap(
                        pav -> pav.getProductAttribute().getName(),
                        pav -> new ProductAttributeValueDTO(pav.getValue())
                ));
    }

    protected ProductMetaDTO mappingProductMeta(Product entity) {
        var prodMeta = productMetaRepository.findByProductId(entity.getId()).orElse(null);
        if (prodMeta == null) return null;
        return ProductMetaDTO.builder()
                .metaDescription(prodMeta.getMetaDescription())
                .attributes(prodMeta.getAttributes())
                .build();
    }
}
