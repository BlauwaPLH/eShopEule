package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.response.BrandStatPagingResponse;
import org.springframework.data.domain.Pageable;

public interface BrandStatisticService {

    BrandStatPagingResponse getBrandStatistics(Pageable pageRequest);
}
