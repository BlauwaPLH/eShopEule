package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.request.ProductOrderStatusStatRequest;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.service.BrandStatisticService;
import org.senju.eshopeule.service.OrderStatisticsService;
import org.senju.eshopeule.service.ProductStatisticsService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.senju.eshopeule.constant.pattern.RoutePattern.PRIVATE_PREFIX;

@RestController
@RequiredArgsConstructor
public class StatisticsController {

    private final BrandStatisticService brandStatisticService;
    private final OrderStatisticsService orderStatisticsService;
    private final ProductStatisticsService productStatisticsService;
    private static final Logger logger = LoggerFactory.getLogger(StatisticsController.class);

    @GetMapping(path = PRIVATE_PREFIX + "/v1/stat/brand")
    @Operation(summary = "Get Brand Statistics")
    public ResponseEntity<? extends BaseResponse> getBrandStatistics(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String[] sortFields,
            @RequestParam(name = "sortDirs", required = false) String[] sortDirections
    ) {
        logger.info("Get Brand Statistics");
        try {
            return ResponseEntity.ok(brandStatisticService.getBrandStatistics(
                    PaginationUtil.findPaginated(pageNo, pageSize, sortFields, sortDirections)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = PRIVATE_PREFIX + "/v1/stat/order")
    @Operation(summary = "Get Order Status Statistics")
    public ResponseEntity<Collection<? extends BaseResponse>> getOrderStatusStatistics() {
        logger.debug("Get Order Status Statistics");
        return ResponseEntity.ok(orderStatisticsService.getOrderStatusStatistics());
    }

    @PostMapping(path = PRIVATE_PREFIX + "/v1/stat/prod/status")
    @Operation(summary = "Get Product (order status) statistics")
    public ResponseEntity<?> getProductOrderStatusStatistics(@Valid @RequestBody ProductOrderStatusStatRequest request) {
        logger.info("Get Product (order status) statistics");
        try {
            return ResponseEntity.ok(productStatisticsService.getProductOrderStatusStatistics(request));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = PRIVATE_PREFIX + "/v1/stat/prod/ordered")
    @Operation(summary = "Get ordered products statistics")
    public ResponseEntity<? extends BaseResponse> getOrderedProductStatistics(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String[] sortFields,
            @RequestParam(name = "sortDirs", required = false) String[] sortDirections
    ) {
        logger.info("Get ordered products statistics");
        try {
            return ResponseEntity.ok(productStatisticsService.getOrderedProductStatistics(
                    PaginationUtil.findPaginated(pageNo, pageSize, sortFields, sortDirections)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = PRIVATE_PREFIX + "/v1/stat/prod/rpr")
    @Operation(summary = "Get repeat purchase rate statistics")
    public ResponseEntity<? extends BaseResponse> getRepeatPurchaseRateStatistics(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize,
            @RequestParam(required = false) String[] sortFields,
            @RequestParam(name = "sortDirs", required = false) String[] sortDirections
    ) {
        logger.info("Get repeat purchase rate statistics");
        try {
            return ResponseEntity.ok(productStatisticsService.getRepeatPurchaseRateStatistics(
                    PaginationUtil.findPaginated(pageNo, pageSize, sortFields, sortDirections)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
