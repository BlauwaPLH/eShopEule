package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.senju.eshopeule.dto.CategoryOrderStatusStatDTO;
import org.senju.eshopeule.dto.CategoryOrderedStatDTO;
import org.senju.eshopeule.dto.response.CategoryOrderedStatPagingResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.repository.jpa.CategoryRepository;
import org.senju.eshopeule.service.CategoryStatisticsService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static org.senju.eshopeule.constant.exceptionMessage.CategoryExceptionMsg.CATEGORY_NOT_FOUND_MSG;
import static org.senju.eshopeule.constant.exceptionMessage.CategoryExceptionMsg.CATEGORY_NOT_FOUND_WITH_ID_MSG;

@Service
@RequiredArgsConstructor
public class CategoryStatisticsServiceImpl implements CategoryStatisticsService {

    private final SqlSession sqlSession;
    private final CategoryRepository categoryRepository;

    private static final String NAMESPACE = "org.senju.mybatis.CategoryXmlMapper";
    private static final Logger logger = LoggerFactory.getLogger(CategoryStatisticsService.class);

    @Override
    public CategoryOrderedStatPagingResponse getCategoryOrderedStat(Pageable pageRequest) {
        final Map<String, String> sortableAttributesMap = Map.of(
                "name", "category_name",
                "products_quantity", "total_quantity",
                "ordered_products_quantity", "total_ordered_quantity",
                "revenue", "total_revenue"
        );
        Map<String, Object> sqlParameters = PaginationUtil.buildSQLPagination(pageRequest, sortableAttributesMap);
        List<CategoryOrderedStatDTO> result = sqlSession.selectList(NAMESPACE + ".categoryOrderedStat", sqlParameters);
        return new CategoryOrderedStatPagingResponse(
                pageRequest.getPageNumber() + 1,
                pageRequest.getPageSize(),
                result
        );
    }

    @Override
    public CategoryOrderStatusStatDTO getCategoryOrderStatusStat(String categoryId) {
        if (categoryId == null || categoryId.isBlank()) throw new NotFoundException(CATEGORY_NOT_FOUND_MSG);
        if (!categoryRepository.existsById(categoryId)) throw new NotFoundException(
                String.format(CATEGORY_NOT_FOUND_WITH_ID_MSG, categoryId)
        );
        return sqlSession.selectOne(NAMESPACE + ".categoryOrderStatusStat", categoryId);
    }
}
