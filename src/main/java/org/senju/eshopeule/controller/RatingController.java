package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.constant.pagination.RatingPageable;
import org.senju.eshopeule.dto.RatingDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.service.RatingService;
import org.senju.eshopeule.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.senju.eshopeule.constant.pattern.RoutePattern.PRIVATE_PREFIX;
import static org.senju.eshopeule.constant.pattern.RoutePattern.PUBLIC_PREFIX;

@RestController
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private static final Logger logger = LoggerFactory.getLogger(RatingController.class);

    @GetMapping(path = PUBLIC_PREFIX + "/v1/rating")
    @Operation(summary = "Get rating by ID")
    public ResponseEntity<? extends BaseResponse> getRatingById(@RequestParam("id") String ratingId) {
        logger.info("Get rating with ID: {}", ratingId);
        try {
            return ResponseEntity.ok(ratingService.getById(ratingId));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = PUBLIC_PREFIX + "/v1/rating/prod")
    @Operation(summary = "Get all ratings by product ID")
    public ResponseEntity<? extends BaseResponse> getAllRatingByProductId(
            @RequestParam("id") String productId,
            @RequestParam(name = "pageNo", required = false, defaultValue = RatingPageable.DEFAULT_PAGE_NO) int pageNo,
            @RequestParam(name = "pageSize", required = false, defaultValue = RatingPageable.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(name = "sortField", required = false, defaultValue = RatingPageable.DEFAULT_SORT_FIELD) String sortField,
            @RequestParam(name = "sortDir", required = false, defaultValue = RatingPageable.DEFAULT_SORT_DIRECTION) String sortDirection
    ) {
        logger.info("Get all ratings with product ID {}", productId);
        try {
            return ResponseEntity.ok(ratingService.getRatingPageByProductId(
                    productId,
                    PaginationUtil.findPaginated(pageNo, pageSize, sortField, sortDirection)
            ));
        } catch (PagingException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = PRIVATE_PREFIX + "/v1/rating")
    @Operation(summary = "Create new rating")
    public ResponseEntity<? extends BaseResponse> createNewRating(@Valid @RequestBody RatingDTO dto) {
        logger.info("Create new rating");
        try {
            return ResponseEntity.ok(ratingService.createNewRating(dto));
        } catch (NotFoundException | ObjectAlreadyExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping(path = PRIVATE_PREFIX + "/v1/rating")
    @Operation(summary = "Update rating")
    public ResponseEntity<? extends BaseResponse> updateRating(@Valid @RequestBody RatingDTO dto) {
        logger.info("Update rating with ID: {}", dto.getId());
        try {
            return ResponseEntity.ok(ratingService.updateRating(dto));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = PRIVATE_PREFIX + "/v1/rating/del")
    @Operation(summary = "Delete rating by ID")
    public ResponseEntity<? extends BaseResponse> deleteById(@RequestParam("id") String ratingId) {
        logger.info("Delete rating with ID: {}", ratingId);
        try {
            ratingService.deleteById(ratingId);
            return ResponseEntity.ok(new SimpleResponse("Delete rating successfully!"));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
