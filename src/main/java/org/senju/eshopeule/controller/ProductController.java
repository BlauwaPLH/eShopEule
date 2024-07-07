package org.senju.eshopeule.controller;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.pagination.ProductPageable;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.service.ProductService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/prod")
public class ProductController {

    private final ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getProductById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String id) {
        logger.debug("Get product with id: {}", id);
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/{prodSlug}")
    public ResponseEntity<? extends BaseResponse> getProductBySlug(@PathVariable("prodSlug") String productSlug) {
        logger.debug("Get product with slug: {}", productSlug);
        try {
            return ResponseEntity.ok(productService.getBySlug(productSlug));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/detail")
    public ResponseEntity<? extends BaseResponse> getProductDetailById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String id) {
        logger.debug("Get product detail with id: {}", id);
        try {
            return ResponseEntity.ok(productService.getProductById(id));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all/brand")
    public ResponseEntity<? extends BaseResponse> getAllProductByBrandId(
            @RequestParam("id") String brandId,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = ProductPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection) {
        logger.debug("Get all product (pageable) with brand id: {}", brandId);
        try {
            return ResponseEntity.ok(
                    productService.getAllProductByBrandId(brandId,
                            PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all/brand/{brandSlug}")
    public ResponseEntity<? extends BaseResponse> getAllProductByBrandSlug(
            @PathVariable("brandSlug") String brandSlug,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = ProductPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection) {
        logger.debug("Get all product (pageable) with brand slug: {}", brandSlug);
        try {
            return ResponseEntity.ok(
                    productService.getAllProductByBrandSlug(brandSlug,
                            PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all/category")
    public ResponseEntity<? extends BaseResponse> getAllProductByCategoryId(
            @RequestParam("id") String categoryId,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = ProductPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection) {
        logger.debug("Get all product (pageable) with category id: {}", categoryId);
        try {
            return ResponseEntity.ok(
                    productService.getAllProductByCategoryId(categoryId,
                            PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection))
            );
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all/category/{categorySlug}")
    public ResponseEntity<? extends BaseResponse> getAllProductByCategorySlug(
            @PathVariable("categorySlug") String categorySlug,
            @RequestParam(name = "pageNo", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = ProductPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = ProductPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = ProductPageable.DEFAULT_SORT_DIRECTION) String sortDirection) {
        logger.debug("Get all product (pageable) with category slug: {}", categorySlug);
        try {
            return ResponseEntity.ok(
                    productService.getAllProductByCategoryId(categorySlug,
                            PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection))
            );
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
