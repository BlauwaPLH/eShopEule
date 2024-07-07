package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.mappers.ProductMetaMapper;
import org.senju.eshopeule.repository.jpa.ProductRepository;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.senju.eshopeule.service.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.senju.eshopeule.constant.exceptionMessage.ProductExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class ProductMetaServiceImpl implements ProductMetaService {

    private final ProductMetaMapper mapper;
    private final ProductMetaRepository productMetaRepository;
    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductMetaService.class);


    @Override
    public ProductMetaDTO getById(String id) throws NotFoundException {
        return mapper.convertToDTO(productMetaRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format(PROD_META_NOT_FOUND_WITH_ID_MSG, id))
        ));
    }

    @Override
    public ProductMetaDTO getByProductId(String productId) throws NotFoundException {
        return mapper.convertToDTO(productMetaRepository.findByProductId(productId).orElseThrow(
                () -> new NotFoundException(String.format(PROD_META_NOT_FOUND_WITH_PRODUCT_ID_MSG, productId))
        ));
    }

    @Override
    public ProductMetaDTO createProdMeta(ProductMetaDTO dto) throws NotFoundException, ObjectAlreadyExistsException {
        if (!productRepository.existsById(dto.getProductId())) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getProductId()));
        }
        if (productMetaRepository.findByProductId(dto.getProductId()).isPresent()) {
            throw new ObjectAlreadyExistsException(String.format(PROD_META_ALREADY_EXISTS_WITH_PRODUCT_ID_MSG, dto.getProductId()));
        }
        return mapper.convertToDTO(productMetaRepository.save(mapper.convertToEntity(dto)));
    }

    @Override
    public ProductMetaDTO updateProdMeta(ProductMetaDTO dto) throws NotFoundException {
        if (dto.getId() == null || !productMetaRepository.existsById(dto.getId())) {
            throw new NotFoundException(PROD_META_NOT_FOUND_MSG);
        }
        if (productRepository.existsById(dto.getProductId())) {
            throw new NotFoundException(String.format(PRODUCT_NOT_FOUND_WITH_ID_MSG, dto.getProductId()));
        }
        return mapper.convertToDTO(productMetaRepository.save(mapper.convertToEntity(dto)));
    }

    @Override
    public void deleteById(String id) {
        productMetaRepository.deleteById(id);
    }

    @Override
    public void deleteByProductId(String productId) {
        productMetaRepository.deleteByProductId(productId);
    }
}
