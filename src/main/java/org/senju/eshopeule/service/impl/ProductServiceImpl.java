package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.mappers.*;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductImageRepository prodImgRepository;
    private final ProductMetaRepository prodMetaRepository;
    private final ImageService imageService;

    private final ProductMetaMapper prodMetaMapper;
    private final ProductSimpleMapper prodSimpleMapper;
    private final ProductPubMapper prodPubMapper;
    private final ProductDetailMapper prodDetailMapper;
    private final ProductPostMapper prodPostMapper;
    private final ProductPutMapper prodPutMapper;

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Override
    public ProductDTO getProductById(String id) {
        return prodPubMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getProductDetailById(String id) {
        return prodDetailMapper.convertToDTO(productRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductDTO getProductBySlug(String productSlug) {
        return prodPubMapper.convertToDTO(productRepository.findBySlug(productSlug).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_SLUG_MSG, productSlug))
        ));
    }

    @Override
    public ProductDTO getProductDetailBySlug(String productSlug) {
        return prodDetailMapper.convertToDTO(productRepository.findBySlug(productSlug).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_SLUG_MSG, productSlug))
        ));
    }

    @Override
    public ProductPagingResponse getAllProductByBrandId(String brandId, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByBrandId(brandId, pageRequest), prodSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByBrandSlug(String brandSlug, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByBrandSlug(brandSlug, pageRequest), prodSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByCategoryId(String categoryId, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByCategoryId(categoryId, pageRequest), prodSimpleMapper);
    }

    @Override
    public ProductPagingResponse getAllProductByCategorySlug(String categorySlug, Pageable pageRequest) {
        return getProductPaging(productRepository.findAllByCategorySlug(categorySlug, pageRequest), prodSimpleMapper);
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
    public ProductDTO createNewProduct(ProductPostDTO dto, MultipartFile[] images) {
        if (productRepository.checkExistsWithSlug(dto.getSlug())) {
            throw new ObjectAlreadyExistsException(String.format(PRODUCT_ALREADY_EXISTS_WITH_SLUG_MSG, dto.getSlug()));
        }
        Product newProduct = prodPostMapper.convertToEntity(dto);

        if (!dto.getHasOptions() || dto.getOptions() == null || dto.getOptions().isEmpty()) {
            newProduct.setHasOptions(false);
        } else {
            newProduct.setHasOptions(true);
            newProduct.setProductOptions(this.createOptions(dto));
        }
        newProduct = productRepository.save(newProduct);
        this.createProductMeta(dto, newProduct.getId());
        this.createProductImages(images, newProduct);

        return prodPubMapper.convertToDTO(newProduct);
    }

    @Override
    public ProductDTO updateProduct(ProductPutDTO dto) {
        Product loadedProduct = productRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getId()))
        );
        prodPutMapper.updateProductFromDTO(dto, loadedProduct);
        return prodPubMapper.convertToDTO(loadedProduct);
    }

    @Override
    public void deleteProductWithId(String productId) {
        Product loadedProduct = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, productId))
        );
        loadedProduct.setIsPublished(false);
        loadedProduct.setIsAllowedToOrder(false);
        productRepository.save(loadedProduct);
    }

    private List<ProductOption> createOptions(ProductPostDTO dto) {
        return dto.getOptions().stream()
                .map(o -> ProductOption.builder()
                        .name(o.getName())
                        .productAttributeValues(this.createAttributeVal(o))
                        .build())
                .collect(Collectors.toList());
    }

    private List<ProductAttributeValue> createAttributeVal(ProductOptionDTO dto) {
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

    private void createProductImages(MultipartFile[] images, Product savedProduct) {
        if (images.length == 0) return;
        Arrays.stream(images).forEach(
                img -> {
                    try {
                        final String imgName = imageService.save(img);
                        final ProductImage newProdImg = ProductImage.builder()
                                .product(savedProduct)
                                .name(imgName)
                                .imageUrl(imageService.getImageUrl(imgName))
                                .build();
                        prodImgRepository.save(newProdImg);
                    } catch (IOException ex) {
                        throw new ProductException("Error upload image");
                    }
                }
        );
    }


    private void createProductMeta(ProductPostDTO dto, String productId) {
        if (dto.getProductMeta() == null) return;
        final ProductMetaDTO productMetaDTO = dto.getProductMeta();
        productMetaDTO.setProductId(productId);
        prodMetaRepository.save(prodMetaMapper.convertToEntity(productMetaDTO));
    }
}
