package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.OrderStatusStatDTO;

import java.util.List;

public interface OrderStatisticsService {

    List<OrderStatusStatDTO> getOrderStatusStatistics();
}
