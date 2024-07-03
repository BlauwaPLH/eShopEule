package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.model.product.ProductMeta;
import org.senju.eshopeule.repository.mongodb.ProductMetaRepository;
import org.senju.eshopeule.service.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductMetaServiceImpl implements ProductMetaService {

    private final ProductMetaRepository productMetaRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductMetaService.class);


    @Override
    public void save(ProductMeta pm) {

    }
}
