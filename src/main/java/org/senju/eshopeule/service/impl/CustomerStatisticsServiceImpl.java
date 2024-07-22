package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.senju.eshopeule.dto.OrderedProdOptionStatDTO;
import org.senju.eshopeule.dto.request.QueryByDateRangeRequest;
import org.senju.eshopeule.dto.response.OrderedProdOptionStatPagingResponse;
import org.senju.eshopeule.service.CustomerStatisticsService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomerStatisticsServiceImpl implements CustomerStatisticsService {

    private final SqlSession sqlSession;

    private static final String NAMESPACE = "org.senju.mybatis.CustomerXmlMapper";

    @Override
    public Integer countCustomersByAgeRange(QueryByDateRangeRequest request) {
        return sqlSession.selectOne(NAMESPACE + ".countCustomersByAgeRange", request);
    }

    @Override
    public List<Map<String, Integer>> countCustomersByAgeGroup() {
        return sqlSession.selectList(NAMESPACE + ".countCustomersByAgeGroup");
    }


    @Override
    public Integer countCompleteCustomerProfiles() {
        return sqlSession.selectOne(NAMESPACE + ".countCompleteCustomerProfiles");
    }

    @Override
    public List<Map<String, Integer>> countCustomersByGender() {
        return sqlSession.selectList(NAMESPACE + ".countCustomersByGender");
    }

    @Override
    public List<Map<String, Integer>> countOrdersByStatusForCustomer() {
        String username = this.extractCurrentUsername();
        return sqlSession.selectList(NAMESPACE + ".countOrdersByStatusForCustomer", username);
    }

    @Override
    public Double getTotalCompletedOrderForCustomer() {
        String username = this.extractCurrentUsername();
        return sqlSession.selectOne(NAMESPACE + ".getTotalCompletedOrderForCustomer", username);
    }

    @Override
    public OrderedProdOptionStatPagingResponse getAllTotalOrderValueByProductAndOption(Pageable pageRequest) {
        Map<String, String> sortablePropertiesMap = Map.of(
                "prodName", "product_name",
                "optName", "option_name",
                "orderItemValue", "total_order_item_value"
        );
        Map<String, Object> sqlParameters = PaginationUtil.buildSQLPagination(pageRequest, sortablePropertiesMap);
        sqlParameters.put("username", this.extractCurrentUsername());

        List<OrderedProdOptionStatDTO>  result = sqlSession.selectList(NAMESPACE + ".getAllTotalOrderValueByProductAndOption", sqlParameters);
        return new OrderedProdOptionStatPagingResponse(
                pageRequest.getPageNumber() + 1,
                pageRequest.getPageSize(),
                result
        );
    }

    private String extractCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
