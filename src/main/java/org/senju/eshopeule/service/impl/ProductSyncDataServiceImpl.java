package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductESDoc;
import org.senju.eshopeule.repository.es.ProductESRepository;
import org.senju.eshopeule.repository.jpa.BrandRepository;
import org.senju.eshopeule.repository.jpa.CategoryRepository;
import org.senju.eshopeule.repository.jpa.ProductImageRepository;
import org.senju.eshopeule.service.ProductSyncDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.PROD_ES_DOC_NOT_FOUND_WITH_PRODUCT_ID_MSG;

@Service
@RequiredArgsConstructor
public class ProductSyncDataServiceImpl implements ProductSyncDataService {

    private final ProductESRepository prodESRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductImageRepository imageRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductSyncDataService.class);

    @Override
    @Transactional
    public void syncData(Product savedEntity, List<String> categoryIds, String brandId) {
        String productId = savedEntity.getId();


        List<String> categoryNames = (categoryIds != null && !categoryIds.isEmpty())
                ? categoryRepository.getAllNamesWithIdList(categoryIds)
                : null;
        String brandName = (brandId != null && !brandId.isBlank())
                ? brandRepository.findNameById(brandId).orElse(null)
                : null;

        List<String> imageUrls = imageRepository.getAllImageUrlByProductId(productId);
        String thumbnailImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);

        ProductESDoc existedDocument = prodESRepository.findByProductId(productId).orElse(null);
        if (existedDocument == null) {
            existedDocument = ProductESDoc.builder()
                    .productId(productId)
                    .name(savedEntity.getName())
                    .slug(savedEntity.getSlug())
                    .price(savedEntity.getPrice())
                    .discount(savedEntity.getDiscount())
                    .imageUrl(thumbnailImageUrl)
                    .isPublished(savedEntity.getIsPublished())
                    .isAllowedToOrder(savedEntity.getIsAllowedToOrder())
                    .brand(brandName)
                    .categories(categoryNames)
                    .build();
        } else {
            existedDocument.setName(savedEntity.getName());
            existedDocument.setSlug(savedEntity.getSlug());
            existedDocument.setPrice(savedEntity.getPrice());
            existedDocument.setDiscount(savedEntity.getDiscount());
            existedDocument.setImageUrl(thumbnailImageUrl);
            existedDocument.setIsPublished(savedEntity.getIsPublished());
            existedDocument.setIsAllowedToOrder(savedEntity.getIsAllowedToOrder());
            existedDocument.setBrand(brandName);
            existedDocument.setCategories(categoryNames);
        }

        prodESRepository.save(existedDocument);
    }

    @Override
    @Transactional
    public void syncImageData(String productId) {
        ProductESDoc loadedDocument = prodESRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(String.format(PROD_ES_DOC_NOT_FOUND_WITH_PRODUCT_ID_MSG, productId))
        );
        List<String> imageUrls = imageRepository.getAllImageUrlByProductId(productId);
        String thumbnailImageUrl = imageUrls.isEmpty() ? null : imageUrls.get(0);
        loadedDocument.setImageUrl(thumbnailImageUrl);
        prodESRepository.save(loadedDocument);
    }
}
