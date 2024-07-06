package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductMetaDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.service.ProductMetaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/pm")
public class ProdMetaController {

    private final ProductMetaService productMetaService;
    private static final Logger logger = LoggerFactory.getLogger(ProdMetaController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getProdMetaById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String prodMetaId) {
        logger.debug("Get by product meta id: {}", prodMetaId);
        try {
            return ResponseEntity.ok(productMetaService.getByProductId(prodMetaId));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/prod")
    public ResponseEntity<? extends BaseResponse> getByProductId(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String productId) {
        logger.debug("Get by product's ID: {}", productId);
        try {
            return ResponseEntity.ok(productMetaService.getByProductId(productId));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> createProductMeta(@Valid @RequestBody ProductMetaDTO dto) {
        logger.debug("Create product meta");
        try {
            return ResponseEntity.ok(productMetaService.createProdMeta(dto));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<? extends BaseResponse> updateProductMeta(@Valid @RequestBody ProductMetaDTO dto) {
        logger.debug("Update product meta");
        try {
            return ResponseEntity.ok(productMetaService.updateProdMeta(dto));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String prodMetaId) {
        productMetaService.deleteById(prodMetaId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/prod/del")
    public ResponseEntity<?> deleteByProductId(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String productId) {
        productMetaService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}
