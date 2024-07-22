package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.senju.eshopeule.dto.BrandStatDTO;
import org.senju.eshopeule.dto.response.BrandStatPagingResponse;
import org.senju.eshopeule.service.BrandStatisticService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BrandStatisticServiceImpl implements BrandStatisticService {

    private final SqlSession sqlSession;
    private static final Map<String, String> sortableAttributesMap;
    private static final String NAMESPACE = "org.senju.mybatis.BrandXmlMapper";

    static {
        sortableAttributesMap = Map.of(
                "name", "brand_name",
                "countProducts", "count_products",
                "totalBrandOrder", "total_brand_order");
    }

    @Override
    public BrandStatPagingResponse getBrandStatistics(Pageable pageRequest) {
        Map<String, Object> sqlParameterMap = new HashMap<>(
                PaginationUtil.buildSQLPagination(pageRequest, sortableAttributesMap)
        );
        List<BrandStatDTO> contents = sqlSession.selectList(NAMESPACE + ".getBrandStat", sqlParameterMap);
        return new BrandStatPagingResponse(
                pageRequest.getPageNumber() + 1,
                pageRequest.getPageSize(),
                contents
        );
    }
}
