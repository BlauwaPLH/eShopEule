package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductOptionDTO;

import java.util.List;

public interface ProductOptionService {

    ProductOptionDTO getById(String id);

    List<ProductOptionDTO> getAllByProductId(String productId);

    ProductOptionDTO createProductOption(ProductOptionDTO dto);

    ProductOptionDTO updateProductOption(ProductOptionDTO dto);

    void deleteById(String id);

    void deleteByProductId(String productId);
}
