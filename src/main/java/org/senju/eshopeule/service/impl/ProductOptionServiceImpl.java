package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.ProductOptionMapper;
import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.model.product.ProductAttributeValue;
import org.senju.eshopeule.model.product.ProductOption;
import org.senju.eshopeule.repository.jpa.ProductAttributeRepository;
import org.senju.eshopeule.repository.jpa.ProductAttributeValueRepository;
import org.senju.eshopeule.repository.jpa.ProductOptionRepository;
import org.senju.eshopeule.repository.jpa.ProductRepository;
import org.senju.eshopeule.repository.projection.SimpleProdAttrView;
import org.senju.eshopeule.service.ProductOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        if (dto.getProductId() == null || dto.getProductId().isBlank()) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MSG);
        }

        if (!productRepository.existsById(dto.getProductId())) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getProductId()));
        }

        ProductOption newOption = mapper.convertToEntity(dto);

        if (dto.getAttributes() != null && !dto.getAttributes().isEmpty()) {
            this.setAttributeVal(dto, newOption);
        }

        return mapper.convertToDTO(optionRepository.save(newOption));
    }

    @Override
    public ProductOptionDTO updateProductOption(ProductOptionDTO dto) {
        if (dto.getProductId() == null || dto.getProductId().isBlank()) {
            throw new NotFoundException(PRODUCT_NOT_FOUND_MSG);
        }
        ProductOption loadedOption = optionRepository.findByOptionIdAndProductId(dto.getId(), dto.getProductId()).orElseThrow(
                () -> new NotFoundException(PROD_OPTION_NOT_FOUND_MSG)
        );

        if (dto.getName() != null) loadedOption.setName(dto.getName());
        if (dto.getAttributes() != null) {
            attributeValueRepository.deleteByOptionId(dto.getId());
            if (!dto.getAttributes().isEmpty()) {
                this.setAttributeVal(dto, loadedOption);
            }
        }
        return mapper.convertToDTO(optionRepository.save(loadedOption));
    }

    private void setAttributeVal(ProductOptionDTO dto, ProductOption entity) {
        List<ProductAttributeValue> valList = new ArrayList<>();
        Map<String, String> attrMap = attributeRepository.getAllWithProductId(dto.getProductId())
                .stream().collect(Collectors.toMap(SimpleProdAttrView::getName, SimpleProdAttrView::getId));

        dto.getAttributes().forEach(
                (attrName, valDTO) -> {
                    if (valDTO.getValue() == null || valDTO.getValue().isBlank()) return;
                    if (!attrMap.containsKey(attrName)) {
                        throw new NotFoundException(String.format(PROD_ATT_NOT_FOUND_WITH_NAME_MSG, attrName));
                    }
                    final ProductAttributeValue val = ProductAttributeValue.builder()
                            .value(valDTO.getValue())
                            .productOption(entity)
                            .productAttribute(
                                    ProductAttribute.builder()
                                    .id(attrMap.get(attrName))
                                    .name(attrName)
                                    .build())
                            .build();
                    valList.add(val);
                }
        );
        if (entity.getId() != null && !entity.getId().isBlank()) {
            attributeValueRepository.saveAll(valList);
        }
        entity.setProductAttributeValues(valList);
    }


    @Override
    public void deleteById(String id) {
        optionRepository.deleteById(id);
    }

}
