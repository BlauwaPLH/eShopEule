package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/po")
public class ProdOptionController {

    private final ProductOptionService optionService;
    private static final Logger logger = LoggerFactory.getLogger(ProdOptionController.class);

    @GetMapping
    @Operation(summary = "Get product option with ID")
    public ResponseEntity<? extends BaseResponse> getProductOption(@RequestParam("id") String optionId) {
        logger.info("Get product option with id: {}", optionId);
        try {
            return ResponseEntity.ok(optionService.getById(optionId));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/prod")
    @Operation(summary = "Get product option with product ID")
    public ResponseEntity<Collection<? extends BaseResponse>> getProductOptionByProductId(@RequestParam("id") String prodId) {
        logger.info("Get all product option with product id: {}", prodId);
        return ResponseEntity.ok(optionService.getAllByProductId(prodId));
    }

    @PostMapping
    @Operation(summary = "Create new product option")
    public ResponseEntity<? extends BaseResponse> createNewProductOption(@Valid @RequestBody ProductOptionDTO dto) {
        logger.info("Create new product option");
        try {
            return ResponseEntity.ok(optionService.createProductOption(dto));
        } catch (NotFoundException | ProductException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    @Operation(summary = "Update product option")
    public ResponseEntity<? extends BaseResponse> updateProductOption(@Valid @RequestBody ProductOptionDTO dto) {
        logger.info("Update product option");
        try {
            return ResponseEntity.ok(optionService.updateProductOption(dto));
        } catch (NotFoundException | ProductException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    @Operation(summary = "Delete with ID")
    public ResponseEntity<?> deleteById(@RequestParam("id") String optionId) {
        logger.info("Delete product option with id: {}", optionId);
        optionService.deleteById(optionId);
        return ResponseEntity.noContent().build();
    }
}
