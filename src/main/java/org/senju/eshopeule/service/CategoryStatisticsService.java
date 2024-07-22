package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.CategoryOrderStatusStatDTO;
import org.senju.eshopeule.dto.response.CategoryOrderedStatPagingResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryStatisticsService {

    CategoryOrderedStatPagingResponse getCategoryOrderedStat(Pageable pageable);

    List<CategoryOrderStatusStatDTO> getCategoryOrderStatusStat(String categoryId);
}
