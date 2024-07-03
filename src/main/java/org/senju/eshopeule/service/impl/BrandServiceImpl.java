package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.BrandDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.mappers.BrandMapper;
import org.senju.eshopeule.model.product.Brand;
import org.senju.eshopeule.repository.jpa.BrandRepository;
import org.senju.eshopeule.service.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.senju.eshopeule.constant.exceptionMessage.BrandExceptionMsg.*;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    private static final Logger logger = LoggerFactory.getLogger(BrandService.class);

    @Override
    public void createNewBrand(BrandDTO dto) throws ObjectAlreadyExistsException {
        Brand newBrand = brandMapper.convertToEntity(dto);
        if (brandRepository.checkBrandExistsWithNameOrSlug(newBrand.getName(), newBrand.getSlug())) {
            throw new ObjectAlreadyExistsException(BRAND_ALREADY_EXISTS_MSG);
        }
        brandRepository.save(newBrand);
    }

    @Override
    @CachePut(cacheNames = "brandCache", key = "#dto.id")
    public BrandDTO updateBrand(BrandDTO dto) throws ObjectAlreadyExistsException, NotFoundException {
        Brand loadedBrand = brandRepository.findById(dto.getId()).orElseThrow(
                () -> new NotFoundException(
                        String.format(BRAND_NOT_FOUND_WITH_ID_MSG, dto.getId())));
        brandMapper.updateFromDTO(dto, loadedBrand);
        if (brandRepository.checkBrandExistsWithNameOrSlug(loadedBrand.getName(), loadedBrand.getSlug())) {
            throw new ObjectAlreadyExistsException(BRAND_ALREADY_EXISTS_MSG);
        }
        return brandMapper.convertToDTO(brandRepository.save(loadedBrand));
    }

    @Override
    @Cacheable(cacheNames = "brandCache", key = "#id")
    public BrandDTO getById(String id) throws NotFoundException {
        return brandMapper.convertToDTO(brandRepository.findById(id).orElseThrow(
                () -> new NotFoundException(
                        String.format(BRAND_NOT_FOUND_WITH_ID_MSG, id)
                )
        ));
    }

    @Override
    public List<BrandDTO> getAllBrand() {
        return brandRepository.findAll().stream()
                .map(brandMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(cacheNames = "brandCache", key = "#id")
    public void deleteById(String id) {
        brandRepository.deleteById(id);
    }
}
