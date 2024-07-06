package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ProductException;

import java.util.List;

public interface ProductOptionService {

    ProductOptionDTO getById(String id) throws NotFoundException;

    List<ProductOptionDTO> getAllByProductId(String productId);

    ProductOptionDTO createProductOption(ProductOptionDTO dto) throws NotFoundException, ProductException;

    ProductOptionDTO updateProductOption(ProductOptionDTO dto) throws NotFoundException, ProductException;

    void deleteById(String id);

    void deleteByProductId(String productId);
}
