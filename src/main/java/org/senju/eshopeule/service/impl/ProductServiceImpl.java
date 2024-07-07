package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.mappers.ProductMapper;
import org.senju.eshopeule.mappers.ProductMetaMapper;
import org.senju.eshopeule.model.product.*;
import org.senju.eshopeule.repository.jpa.ProductAttributeRepository;
import org.senju.eshopeule.repository.jpa.ProductImageRepository;
import org.senju.eshopeule.repository.jpa.ProductRepository;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.senju.eshopeule.service.ImageService;
import org.senju.eshopeule.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMetaRepository productMetaRepository;
    private final ImageService imageService;
    private final ProductMetaMapper productMetaMapper;
    private final ProductMapper<ProductSimpleDTO> productSimpleMapper;
    private final ProductMapper<ProductPubDTO> productPubMapper;
    private final ProductMapper<ProductDetailDTO> productDetailMapper;
    private final ProductMapper<ProductPostDTO> productPostMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public ProductDTO getProductById(String id) throws NotFoundException {
        return productPubMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getProductDetailById(String id) throws NotFoundException {
        return productDetailMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getProductBySlug(String productSlug) throws NotFoundException {
        return productPubMapper.convertToDTO(productRepository.findBySlug(productSlug).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_SLUG_MSG, productSlug))
        ));
    }

    @Override
    public ProductDTO getProductDetailBySlug(String productSlug) throws NotFoundException {
        return productDetailMapper.convertToDTO(productRepository.findBySlug(productSlug).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_SLUG_MSG, productSlug))
        ));
    }

    @Override
    public ProductPagingResponse getAllProductByBrandId(String brandId, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByBrandId(brandId, pageRequest), productSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByBrandSlug(String brandSlug, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByBrandSlug(brandSlug, pageRequest), productSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByCategoryId(String categoryId, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByCategoryId(categoryId, pageRequest), productSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByCategorySlug(String categorySlug, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByCategorySlug(categorySlug, pageRequest), productSimpleMapper);
    }

    private ProductPagingResponse getProductPaging(Page<Product> productPage, ProductMapper<? extends ProductDTO> mapper) {
        return ProductPagingResponse.builder()
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .isLast(productPage.isLast())
                .pageNo(productPage.getPageable().getPageNumber() + 1)
                .pageSize(productPage.getPageable().getPageSize())
                .products(
                        productPage.getContent().stream()
                                .map(mapper::convertToDTO)
                                .toList()
                )
                .build();
    }

    @Override
    public ProductDTO createNewProduct(ProductPostDTO dto) throws NotFoundException, ObjectAlreadyExistsException, ProductException {
        if (productRepository.checkExistsWithSlug(dto.getSlug())) {
            throw new ObjectAlreadyExistsException(String.format(PRODUCT_ALREADY_EXISTS_WITH_SLUG_MSG, dto.getSlug()));
        }
        Product newProduct = productPostMapper.convertToEntity(dto);

        if (!dto.getHasOptions() || dto.getOptions() == null || dto.getOptions().isEmpty()) {
            newProduct.setHasOptions(false);
        } else {
            newProduct.setHasOptions(true);
            newProduct.setProductOptions(this.createOptions(dto));
        }
        newProduct = productRepository.save(newProduct);
        this.createProductMeta(dto, newProduct.getId());
        return productPubMapper.convertToDTO(newProduct);
    }

    private List<ProductOption> createOptions(ProductPostDTO dto) throws ProductException {
        return dto.getOptions().stream()
                .map(o -> ProductOption.builder()
                        .name(o.getName())
                        .productAttributeValues(this.createAttributeVal(o))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProductAttributeValue> createAttributeVal(ProductOptionDTO dto) throws ProductException {
        if (dto.getAttributes() == null || dto.getAttributes().isEmpty()) return null;
        final List<ProductAttributeValue> attributeValues = new ArrayList<>();
        dto.getAttributes().forEach(
                (attrName, valDTO) -> {
                    final ProductAttribute attr = attributeRepository.findByName(attrName).orElseThrow(
                            () -> new ProductException(String.format(PROD_ATT_NOT_FOUND_WITH_NAME_MSG, attrName))
                    );
                    final ProductAttributeValue value = ProductAttributeValue.builder()
                            .value(valDTO.getValue())
                            .productAttribute(attr)
                            .build();
                    attributeValues.add(value);
                }
        );
        return attributeValues;
    }


    private void createProductMeta(ProductPostDTO dto, String productId) {
        if (dto.getProductMeta() == null) return;
        final ProductMetaDTO productMetaDTO = dto.getProductMeta();
        productMetaDTO.setProductId(productId);
        productMetaRepository.save(productMetaMapper.convertToEntity(productMetaDTO));
    }
}
