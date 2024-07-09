package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductMetaDTO;

public interface ProductMetaService {

    ProductMetaDTO getById(String id);

    ProductMetaDTO getByProductId(String productId);

    ProductMetaDTO createProdMeta(ProductMetaDTO dto);

    ProductMetaDTO updateProdMeta(ProductMetaDTO dto);

    void deleteById(String id);

    void deleteByProductId(String productId);
}
