package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.ProdOrderStatusStatDTO;
import org.senju.eshopeule.dto.request.ProductOrderStatusStatRequest;
import org.senju.eshopeule.dto.response.OrderedProductStatPagingResponse;
import org.senju.eshopeule.dto.response.ProductRepeatPurchaseRatePagingResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductStatisticsService {

    List<ProdOrderStatusStatDTO> getProductOrderStatusStatistics(ProductOrderStatusStatRequest request);

    OrderedProductStatPagingResponse getOrderedProductStatistics(Pageable pageRequest);

    ProductRepeatPurchaseRatePagingResponse getRepeatPurchaseRateStatistics(Pageable pageRequest);
}

