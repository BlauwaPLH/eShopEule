package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.service.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/pm")
public class ProdMetaController {

    private final ProductMetaService productMetaService;
    private static final Logger logger = LoggerFactory.getLogger(ProdMetaController.class);

    @GetMapping
    @Operation(summary = "Get product meta with ID")
    public ResponseEntity<? extends BaseResponse> getProdMetaById(@RequestParam("id") String prodMetaId) {
        logger.info("Get by product meta id: {}", prodMetaId);
        try {
            return ResponseEntity.ok(productMetaService.getById(prodMetaId));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/prod")
    @Operation(summary = "Get product meta with product ID")
    public ResponseEntity<? extends BaseResponse> getByProductId(@RequestParam("id") String productId) {
        logger.info("Get by product's ID: {}", productId);
        try {
            return ResponseEntity.ok(productMetaService.getByProductId(productId));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Create new product meta")
    public ResponseEntity<? extends BaseResponse> createProductMeta(@Valid @RequestBody ProductMetaDTO dto) {
        logger.info("Create product meta");
        try {
            return ResponseEntity.ok(productMetaService.createProdMeta(dto));
        } catch (NotFoundException | ObjectAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    @Operation(summary = "Update product meta")
    public ResponseEntity<? extends BaseResponse> updateProductMeta(@Valid @RequestBody ProductMetaDTO dto) {
        logger.info("Update product meta with product meta ID: {}", dto.getProductId());
        try {
            return ResponseEntity.ok(productMetaService.updateProdMeta(dto));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    @Operation(summary = "Delete product meta with ID")
    public ResponseEntity<?> deleteById(@RequestParam("id") String prodMetaId) {
        logger.info("Delete with product meta ID: {}", prodMetaId);
        productMetaService.deleteById(prodMetaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/prod/del")
    @Operation(summary = "Delete product meta with product ID")
    public ResponseEntity<?> deleteByProductId(@RequestParam("id") String productId) {
        logger.info("Delete with product ID: {}", productId);
        productMetaService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
