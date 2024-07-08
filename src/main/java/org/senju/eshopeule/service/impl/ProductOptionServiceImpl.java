package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.mappers.ProductOptionMapper;
import org.senju.eshopeule.model.product.Product;
import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.model.product.ProductAttributeValue;
import org.senju.eshopeule.model.product.ProductOption;
import org.senju.eshopeule.repository.jpa.ProductAttributeRepository;
import org.senju.eshopeule.repository.jpa.ProductAttributeValueRepository;
import org.senju.eshopeule.repository.jpa.ProductOptionRepository;
import org.senju.eshopeule.repository.jpa.ProductRepository;
import org.senju.eshopeule.service.ProductOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductOptionServiceImpl implements ProductOptionService {

    private final ProductOptionMapper mapper;
    private final ProductRepository productRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductAttributeRepository attributeRepository;
    private final ProductAttributeValueRepository attributeValueRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductOptionService.class);

    @Override
    public ProductOptionDTO getById(String id) {
        return mapper.convertToDTO(optionRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PROD_OPTION_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public List<ProductOptionDTO> getAllByProductId(String productId) {
        return optionRepository.findAllByProductId(productId).stream()
                .map(mapper::convertToDTO)
                .toList();
    }

    @Override
    public ProductOptionDTO createProductOption(ProductOptionDTO dto) {
        if (dto.getProductId() == null) throw new NotFoundException(PRODUCT_NOT_FOUND_MSG);
        final Product product = productRepository.findById(dto.getProductId()).orElseThrow(
                () -> new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getProductId()))
        );
        ProductOption newProdOption = mapper.convertToEntity(dto);
        List<ProductAttributeValue> newAttributeValues = new ArrayList<>();
        if (!newProdOption.getProductAttributeValues().isEmpty()) {
            for (ProductAttributeValue val: newProdOption.getProductAttributeValues()) {
                final String attributeName = val.getProductAttribute().getName();
                final ProductAttribute attribute = attributeRepository.findByName(attributeName).orElseThrow(
                        () -> new NotFoundException(String.format(PROD_ATT_NOT_FOUND_WITH_NAME_MSG, attributeName))
                );
                if (!attributeRepository.checkCategoryContainsProductAndAttribute(product.getId(), attribute.getId())) {
                    throw new ProductException(String.format(CATEGORY_NOT_CONTAINING_PROD_ATT_MSG, product.getId(), attribute.getId()));
                }
                val.setProductAttribute(attribute);
                val = attributeValueRepository.save(val);
                newAttributeValues.add(val);
            }
        }
        newProdOption.setProductAttributeValues(newAttributeValues);
        return mapper.convertToDTO(optionRepository.save(newProdOption));
    }

    @Override
    public ProductOptionDTO updateProductOption(ProductOptionDTO dto) {
        ProductOption loadedOption = optionRepository.findByOptionIdAndProductId(dto.getId(), dto.getProductId()).orElseThrow(
                () -> new NotFoundException(PROD_OPTION_NOT_FOUND_MSG)
        );
        if (dto.getName() != null) loadedOption.setName(dto.getName());
        if (dto.getAttributes() == null || dto.getAttributes().isEmpty()) {
            attributeValueRepository.deleteByOptionId(dto.getId());
        }
        else {
            dto.getAttributes().forEach(
                    (attrName, val) -> {
                        ProductAttribute attribute = attributeRepository.findByName(attrName).orElseThrow(
                                () -> new ProductException(String.format(PROD_ATT_NOT_FOUND_WITH_NAME_MSG, attrName))
                        );
                        if (val.getId() != null) {
                            if (val.getValue() == null) {
                                attributeValueRepository.deleteById(val.getId());
                            } else {
                                attributeValueRepository.updateAttributeValue(
                                        val.getId(), loadedOption.getId(),
                                        attribute.getId(), val.getValue()
                                );
                            }
                        } else {
                            if (val.getValue() != null && !val.getValue().isBlank()) {
                                ProductAttributeValue newVal = ProductAttributeValue.builder()
                                        .value(val.getValue())
                                            .productAttribute(attribute)
                                            .productOption(loadedOption)
                                            .build();
                                    attributeValueRepository.save(newVal);
                                }
                            }
                        }
                );
            }
        return mapper.convertToDTO(optionRepository.save(loadedOption));
    }

    @Override
    public void deleteById(String id) {
        optionRepository.deleteById(id);
    }

    @Override
    public void deleteByProductId(String productId) {
        optionRepository.deleteByProductId(productId);
    }
}
