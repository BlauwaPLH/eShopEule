package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.mappers.ProductMetaMapper;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.senju.eshopeule.service.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMetaServiceImpl implements ProductMetaService {

    private final ProductMetaMapper mapper;
    private final ProductMetaRepository productMetaRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductMetaService.class);


    @Override
    public ProductMetaDTO getById(String id) throws NotFoundException {
        return null;
    }

    @Override
    public ProductMetaDTO getByProductId(String productId) throws NotFoundException {
        return null;
    }

    @Override
    public ProductMetaDTO createProdMeta(ProductMetaDTO dto) throws NotFoundException {
        return null;
    }

    @Override
    public ProductMetaDTO updateProdMeta(ProductMetaDTO dto) throws NotFoundException {
        return null;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void deleteByProductId(String productId) {

    }
}
