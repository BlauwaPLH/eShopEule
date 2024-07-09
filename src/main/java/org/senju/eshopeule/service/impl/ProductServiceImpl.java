package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.*;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.mappers.*;
import org.senju.eshopeule.model.product.*;
import org.senju.eshopeule.repository.jpa.*;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.senju.eshopeule.repository.projection.SimpleProdAttrView;
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
import java.util.Map;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.BrandExceptionMsg.BRAND_NOT_FOUND_WITH_ID_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CategoryExceptionMsg.CATEGORY_NOT_FOUND_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductImageRepository prodImgRepository;
    private final ProductMetaRepository prodMetaRepository;
    private final ProductCategoryRepository prodCategoryRepository;
    private final ImageService imageService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

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

        this.createProductBrand(newProduct, dto);
        this.createProductCategories(newProduct, dto);

        if (dto.getOptions() == null || dto.getOptions().isEmpty()) {
            newProduct.setHasOptions(false);
        } else {
            newProduct.setHasOptions(true);
            this.createOptions(newProduct, dto);
        }

        newProduct = productRepository.save(newProduct);

        this.createProductMeta(dto, newProduct.getId());
        this.createProductImages(images, newProduct);

        return prodSimpleMapper.convertToDTO(newProduct);
    }

    private void createProductBrand(Product newProduct, ProductPostDTO dto) {
        setProductBrand(newProduct, dto.getBrandId());
    }

    private void createProductCategories(Product newProduct, ProductPostDTO dto) {
        if (dto.getCategoryIds() == null || dto.getCategoryIds().isEmpty()) return;
        int existingCategoryCount = categoryRepository.countCategoriesWithIds(dto.getCategoryIds());
        if (existingCategoryCount != dto.getCategoryIds().size()) {
            throw new NotFoundException(CATEGORY_NOT_FOUND_MSG);
        }
        final List<ProductCategory> productCategoryList = new ArrayList<>();
        dto.getCategoryIds().forEach(
                cId -> {
                    final ProductCategory pc = ProductCategory.builder()
                            .category(Category.builder().id(cId).build())
                            .product(newProduct)
                            .build();
                    productCategoryList.add(pc);
                }
        );
        newProduct.setProductCategories(productCategoryList);
    }

    private void createOptions(Product newProduct, ProductPostDTO dto) {
        newProduct.setProductOptions(
                dto.getOptions()
                        .stream()
                        .map(optionDTO -> {
                            final ProductOption entity = ProductOption.builder()
                                    .name(optionDTO.getName())
                                    .product(newProduct)
                                    .build();
                            this.createAttributeVal(entity, optionDTO, dto.getCategoryIds());
                            return entity;
                        })
                        .collect(Collectors.toList()));
    }

    private void createAttributeVal(ProductOption entity, ProductOptionDTO dto, List<String> categoryIds) {
        logger.debug("Create attribute values");
        if (dto.getAttributes() == null || dto.getAttributes().isEmpty()) return;
        final List<ProductAttributeValue> attributeValues = new ArrayList<>();
        Map<String, String> attrMap = attributeRepository.getAllWithCategoryIds(categoryIds)
                .stream().collect(Collectors.toMap(SimpleProdAttrView::getName, SimpleProdAttrView::getId));
        dto.getAttributes().forEach(
                (attrName, valDTO) -> {
                    if (!attrMap.containsKey(attrName)) throw new NotFoundException(String.format(PROD_ATT_NOT_FOUND_WITH_NAME_MSG, attrName));
                    final ProductAttributeValue value = ProductAttributeValue.builder()
                            .value(valDTO.getValue())
                            .productAttribute(ProductAttribute.builder().id(attrMap.get(attrName)).build())
                            .productOption(entity)
                            .build();
                    attributeValues.add(value);
                }
        );
        entity.setProductAttributeValues(attributeValues);
    }

    private void createProductImages(MultipartFile[] images, Product savedProduct) {
        if (images == null || images.length == 0) return;
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

    @Override
    public ProductDTO updateProduct(ProductPutDTO dto) {
        Product loadedProduct = productRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getId()))
        );
        if (dto.getSlug() != null && !dto.getSlug().isBlank()) {
            if (productRepository.checkExistsWithSlugExceptId(dto.getId(), dto.getSlug())) {
                throw new ObjectAlreadyExistsException(String.format(PRODUCT_ALREADY_EXISTS_WITH_SLUG_MSG, dto.getSlug()));
            }
        }
        prodPutMapper.updateProductFromDTO(dto, loadedProduct);
        this.updateProductBrand(loadedProduct, dto);
        this.updateProductCategories(loadedProduct, dto);
        loadedProduct = productRepository.save(loadedProduct);

        return prodSimpleMapper.convertToDTO(loadedProduct);
    }

    private void updateProductBrand(Product loadedProduct, ProductPutDTO dto) {
        if (dto.getBrandId() == null) return;
        if (dto.getBrandId().isBlank()) {
            loadedProduct.setBrand(null);
        } else {
            setProductBrand(loadedProduct, dto.getBrandId());
        }
    }

    private void setProductBrand(Product entity, String brandId) {
        if (!brandRepository.existsById(brandId)) throw new NotFoundException(String.format(BRAND_NOT_FOUND_WITH_ID_MSG, brandId));
        entity.setBrand(Brand.builder().id(brandId).build());
    }

    private void updateProductCategories(Product loadedProduct, ProductPutDTO dto) {
        if (dto.getCategoryIds() == null) return;
        if (dto.getCategoryIds().isEmpty()) {
            prodCategoryRepository.deleteByProductId(loadedProduct.getId());
            loadedProduct.setProductCategories(null);
        } else {
            int existingCategoryCount = categoryRepository.countCategoriesWithIds(dto.getCategoryIds());
            if (existingCategoryCount != dto.getCategoryIds().size()) {
                throw new NotFoundException(CATEGORY_NOT_FOUND_MSG);
            }
            prodCategoryRepository.deleteWithCategoryIdNotInList(loadedProduct.getId(), dto.getCategoryIds());
            dto.getCategoryIds().forEach(
                    cId -> {
                        if (!prodCategoryRepository.checkExistsWithProductIdAndCategoryId(loadedProduct.getId(), cId)) {
                            final ProductCategory newProdCate = ProductCategory.builder()
                                    .category(Category.builder().id(cId).build())
                                    .product(loadedProduct)
                                    .build();
                            prodCategoryRepository.save(newProdCate);
                        }
                    }
            );
        }
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
}
