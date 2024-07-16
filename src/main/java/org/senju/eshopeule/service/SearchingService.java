package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.response.ProductSearchResultResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchingService {

    ProductSearchResultResponse search(String keyword, String brandName, List<String> categoryNames,
                                       Double minPrice, Double maxPrice, Pageable pageRequest);
}
