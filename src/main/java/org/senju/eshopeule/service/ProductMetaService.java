package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;

public interface ProductMetaService {

    ProductMetaDTO getById(String id) throws NotFoundException;

    ProductMetaDTO getByProductId(String productId) throws NotFoundException;

    ProductMetaDTO createProdMeta(ProductMetaDTO dto) throws NotFoundException, ObjectAlreadyExistsException;

    ProductMetaDTO updateProdMeta(ProductMetaDTO dto) throws NotFoundException;

    void deleteById(String id);

    void deleteByProductId(String productId);
}
