package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.response.OrderedProdOptionStatPagingResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomerStatisticsService {

    Integer countCustomersByAgeRange(int minAge, int maxAge);

    List<Map<String, Integer>> countCustomersByAgeGroup();

    Integer countCompleteCustomerProfiles();

    List<Map<String, Integer>> countCustomersByGender();

    List<Map<String, Integer>> countOrdersByStatusForCustomer();

    Double getTotalCompletedOrderForCustomer();

    OrderedProdOptionStatPagingResponse getAllTotalOrderValueByProductAndOption(Pageable pageRequest);
}
