package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.pagination.OrderPageable;
import org.senju.eshopeule.constant.pagination.ProductPageable;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.service.SearchingService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/search")
public class SearchController {

    private final SearchingService searchingService;
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> search(
            @RequestParam(value = "query", defaultValue = "") String keyword,
            @RequestParam(value = "brand", required = false) String brandName,
            @RequestParam(value = "categories", required = false) List<String> categoryNames,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = ProductPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        return ResponseEntity.ok(searchingService.search(keyword, brandName, categoryNames, minPrice, maxPrice,
                PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)));
    }
}
