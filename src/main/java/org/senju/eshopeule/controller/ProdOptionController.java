package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductOptionDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.service.ProductOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/po")
public class ProdOptionController {

    private final ProductOptionService optionService;
    private static final Logger logger = LoggerFactory.getLogger(ProdOptionController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getProductOption(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String optionId) {
        logger.debug("Get product option with id: {}", optionId);
        try {
            return ResponseEntity.ok(optionService.getById(optionId));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/prod")
    public ResponseEntity<Collection<? extends BaseResponse>> getProductOptionByProductId(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String prodId) {
        logger.debug("Get all product option with product id: {}", prodId);
        return ResponseEntity.ok(optionService.getAllByProductId(prodId));
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewProductOption(@Valid @RequestBody ProductOptionDTO dto) {
        logger.debug("Create new product option");
        try {
            return ResponseEntity.ok(optionService.createProductOption(dto));
        } catch (NotFoundException | ProductException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<? extends BaseResponse> updateProductOption(@Valid @RequestBody ProductOptionDTO dto) {
        logger.debug("Update product option");
        try {
            return ResponseEntity.ok(optionService.updateProductOption(dto));
        } catch (NotFoundException | ProductException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/prod/del")
    public ResponseEntity<?> deleteByProductId(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String productId) {
        logger.debug("Delete all product option with product's id: {}", productId);
        optionService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String optionId) {
        logger.debug("Delete product option with id: {}", optionId);
        optionService.deleteById(optionId);
        return ResponseEntity.noContent().build();
    }
}
