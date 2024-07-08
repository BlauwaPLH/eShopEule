package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductAttributeDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.mappers.ProductAttributeMapper;
import org.senju.eshopeule.model.product.ProductAttribute;
import org.senju.eshopeule.repository.jpa.ProductAttributeRepository;
import org.senju.eshopeule.service.ProductAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeMapper mapper;
    private final ProductAttributeRepository productAttributeRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductAttributeService.class);

    @Override
    @Cacheable(cacheNames = "productAttributeCache", key = "#id")
    public ProductAttributeDTO getById(String id) {
        return mapper.convertToDTO(productAttributeRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PROD_ATT_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public List<ProductAttributeDTO> getAllProdAtt() {
        return productAttributeRepository.findAll().stream()
                .map(v -> new ProductAttributeDTO(v.getId(), v.getName()))
                .toList();
    }

    @Override
    public List<ProductAttributeDTO> getAllProdAttWithCategoryId(String categoryId) {
        return productAttributeRepository.getAllProdAttrWithCategoryId(categoryId).stream()
                .map(v -> new ProductAttributeDTO(v.getId(), v.getName()))
                .toList();
    }

    @Override
    public void createNewProdAtt(ProductAttributeDTO dto) {
        if (productAttributeRepository.checkProdAttrExistsWithName(dto.getName())) {
            throw new ObjectAlreadyExistsException(PROD_ATT_ALREADY_EXISTS_MSG);
        }
        productAttributeRepository.save(mapper.convertToEntity(dto));
    }

    @Override
    @CachePut(cacheNames = "productAttributeCache", key = "#dto.id")
    public ProductAttributeDTO updateProdAtt(ProductAttributeDTO dto) {
        ProductAttribute loadedEntity = productAttributeRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(String.format(PROD_ATT_NOT_FOUND_WITH_ID_MSG, dto.getId()))
        );
        if (dto.getName() != null) {
            if (productAttributeRepository.checkProdAttrExistsWithNameExceptId(dto.getName(), dto.getId())) {
                throw new ObjectAlreadyExistsException(PROD_ATT_ALREADY_EXISTS_MSG);
            }
            loadedEntity.setName(dto.getName());
        }
        return mapper.convertToDTO(productAttributeRepository.save(loadedEntity));
    }

    @Override
    @CacheEvict(cacheNames = "productAttributeCache", key = "#id")
    public void deleteById(String id) {
        productAttributeRepository.deleteById(id);
    }
}
