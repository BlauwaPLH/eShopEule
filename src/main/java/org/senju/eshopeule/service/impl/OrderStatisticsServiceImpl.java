package org.senju.eshopeule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.senju.eshopeule.dto.OrderStatusStatDTO;
import org.senju.eshopeule.service.OrderStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatisticsServiceImpl implements OrderStatisticsService {

    private final SqlSession sqlSession;
    private static final String NAMESPACE = "org.senju.mybatis.OrderXmlMapper";
    private static final Logger logger = LoggerFactory.getLogger(OrderStatisticsService.class);

    @Override
    public List<OrderStatusStatDTO> getOrderStatusStatistics() {
        return sqlSession.selectList(NAMESPACE + ".getOrderStatusStatistics");
    }
}
