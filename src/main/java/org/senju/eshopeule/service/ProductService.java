package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductDTO;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductDTO getProductById(String id) throws NotFoundException;

    ProductDTO getProductDetailById(String id) throws NotFoundException;

    ProductDTO getBySlug(String slug) throws NotFoundException;

    ProductPagingResponse getAllProductByBrandId(String brandId, Pageable pageRequest);

    ProductPagingResponse getAllProductByBrandSlug(String brandSlug, Pageable pageRequest);

    ProductPagingResponse getAllProductByCategoryId(String categoryId, Pageable pageRequest);

    ProductPagingResponse getAllProductByCategorySlug(String categorySlug, Pageable pageRequest);

}
