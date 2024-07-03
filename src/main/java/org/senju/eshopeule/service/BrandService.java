package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.BrandDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;

import java.util.List;

public interface BrandService {

    void createNewBrand(BrandDTO dto) throws ObjectAlreadyExistsException;

    BrandDTO updateBrand(BrandDTO dto) throws ObjectAlreadyExistsException, NotFoundException;

    BrandDTO getById(String id) throws NotFoundException;

    List<BrandDTO> getAllBrand();

    void deleteById(String id);
}
