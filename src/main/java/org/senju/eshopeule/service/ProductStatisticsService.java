package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProdOrderStatusStatDTO;
import org.senju.eshopeule.dto.response.OrderedProductStatPagingResponse;
import org.senju.eshopeule.dto.response.ProductRepeatPurchaseRatePagingResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductStatisticsService {

    List<ProdOrderStatusStatDTO> getProductOrderStatusStatistics(String productId, LocalDateTime startDate, LocalDateTime endDate);

    OrderedProductStatPagingResponse getOrderedProductStatistics(Pageable pageRequest);

    ProductRepeatPurchaseRatePagingResponse getRepeatPurchaseRateStatistics(Pageable pageRequest);
}

