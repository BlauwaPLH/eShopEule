package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductAttributeDTO;

import java.util.List;

public interface ProductAttributeService {

    ProductAttributeDTO getById(String id);

    List<ProductAttributeDTO> getAllProdAtt();

    List<ProductAttributeDTO> getAllProdAttWithCategoryId(String categoryId);

    void createNewProdAtt(ProductAttributeDTO dto);

    ProductAttributeDTO updateProdAtt(ProductAttributeDTO dto);

    void deleteById(String id);
}
