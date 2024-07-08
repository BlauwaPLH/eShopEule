package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProductDTO;
import org.senju.eshopeule.dto.ProductPostDTO;
import org.senju.eshopeule.dto.ProductPutDTO;
import org.senju.eshopeule.dto.response.ProductPagingResponse;
import org.springframework.data.domain.Pageable;


public interface ProductService {
    ProductDTO getProductById(String id);

    ProductDTO getProductDetailById(String id);

    ProductDTO getProductBySlug(String slug);

    ProductDTO getProductDetailBySlug(String slug);

    ProductPagingResponse getAllProductByBrandId(String brandId, Pageable pageRequest);

    ProductPagingResponse getAllProductByBrandSlug(String brandSlug, Pageable pageRequest);

    ProductPagingResponse getAllProductByCategoryId(String categoryId, Pageable pageRequest);

    ProductPagingResponse getAllProductByCategorySlug(String categorySlug, Pageable pageRequest);

    ProductDTO createNewProduct(ProductPostDTO dto);

    ProductDTO updateProduct(ProductPutDTO dto);

    void deleteProductWithId(String productId);
}
