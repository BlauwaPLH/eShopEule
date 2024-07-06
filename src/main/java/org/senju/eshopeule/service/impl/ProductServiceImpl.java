package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductDTO;
import org.senju.eshopeule.dto.ProductSimpleDTO;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.ProductMapper;
import org.senju.eshopeule.model.product.Product;
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

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.PRODUCT_NOT_FOUND_WITH_ID_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.PRODUCT_NOT_FOUND_WITH_SLUG_MSG;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper<ProductSimpleDTO> productSimpleMapper;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMetaRepository productMetaRepository;
    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public ProductDTO getProductById(String id) throws NotFoundException {
        return productSimpleMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getProductDetailById(String id) throws NotFoundException {
        return productSimpleMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getBySlug(String slug) throws NotFoundException {
        return productSimpleMapper.convertToDTO(productRepository.findBySlug(slug).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_SLUG_MSG, slug))
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
}
