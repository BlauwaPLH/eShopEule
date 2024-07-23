package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.pagination.ProductPageable;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.service.SearchingService;
import org.senju.eshopeule.utils.PaginationUtil;
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

    @GetMapping
    public ResponseEntity<? extends BaseResponse> search(
            @RequestParam(value = "query", defaultValue = "") String keyword,
            @RequestParam(value = "brand", required = false) String brandName,
            @RequestParam(value = "categories", required = false) List<String> categoryNames,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = "price") String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        try {
            return ResponseEntity.ok(searchingService.search(keyword, brandName, categoryNames, minPrice, maxPrice,
                    PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)));
        } catch (PagingException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }

    }
}
