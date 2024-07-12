package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProductAttributeDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.service.ProductAttributeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/pa")
public class ProdAttributeController {

    private final ProductAttributeService productAttributeService;
    private static final Logger logger = LoggerFactory.getLogger(ProdAttributeController.class);

    @GetMapping
    @Operation(description = "Get attribute with ID")
    public ResponseEntity<? extends BaseResponse> getProdAttrById(@RequestParam("id") String id) {
        try {
            logger.info("Get product attribute with id: {}", id);
            return ResponseEntity.ok(productAttributeService.getById(id));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all")
    @Operation(description = "Get all product attributes")
    public ResponseEntity<Collection<? extends BaseResponse>> getAllProdAttr() {
        logger.info("Get all product attributes");
        return ResponseEntity.ok(productAttributeService.getAllProdAtt());
    }

    @GetMapping(path = "/cate")
    @Operation(description = "Get all attribute with category ID")
    public ResponseEntity<Collection<? extends BaseResponse>> getAllProdAttrWithCategoryId(@RequestParam("id") String id) {
        logger.info("Get all product attributes with category id: {}", id);
        return ResponseEntity.ok(productAttributeService.getAllProdAttWithCategoryId(id));
    }

    @PostMapping
    @Operation(description = "Create new attribute")
    public ResponseEntity<? extends BaseResponse> createNewProdAttr(@Valid @RequestBody ProductAttributeDTO dto) {
        try {
            logger.info("Create new product attribute");
            productAttributeService.createNewProdAtt(dto);
            return ResponseEntity.ok(new SimpleResponse("Create new product attribute successfully!"));
        } catch (ObjectAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    @Operation(description = "Update attribute")
    public ResponseEntity<? extends BaseResponse> updateProdAttr(@Valid @RequestBody ProductAttributeDTO dto) {
        try {
            logger.info("Update product attribute");
            return ResponseEntity.ok(productAttributeService.updateProdAtt(dto));
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    @Operation(description = "Delete attribute with ID")
    public ResponseEntity<?> deleteById(@RequestParam("id") String id) {
        logger.info("Delete product attribute with id: {}", id);
        productAttributeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
