package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.CategoryOrderStatusStatDTO;
import org.senju.eshopeule.dto.response.CategoryOrderedStatPagingResponse;
import org.springframework.data.domain.Pageable;

public interface CategoryStatisticsService {

    CategoryOrderedStatPagingResponse getCategoryOrderedStat(Pageable pageable);

    CategoryOrderStatusStatDTO getCategoryOrderStatusStat(String categoryId);
}
