package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductAttributeDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;

import java.util.List;

public interface ProductAttributeService {

    ProductAttributeDTO getById(String id) throws NotFoundException;

    List<ProductAttributeDTO> getAllProdAtt();

    List<ProductAttributeDTO> getAllProdAttWithCategoryId(String categoryId);

    void createNewProdAtt(ProductAttributeDTO dto) throws ObjectAlreadyExistsException;

    ProductAttributeDTO updateProdAtt(ProductAttributeDTO dto) throws ObjectAlreadyExistsException, NotFoundException;

    void deleteById(String id);
}
