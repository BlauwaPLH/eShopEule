package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.BrandDTO;

import java.util.List;

public interface BrandService {

    void createNewBrand(BrandDTO dto);

    BrandDTO updateBrand(BrandDTO dto);

    BrandDTO getById(String id);

    List<BrandDTO> getAllBrand();

    void deleteById(String id);
}
