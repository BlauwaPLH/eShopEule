package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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

import static org.senju.eshopeule.constant.pattern.RegexPattern.ID_PATTERN;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/prod-attr")
public class ProdAttributeController {

    private final ProductAttributeService productAttributeService;
    private static final Logger logger = LoggerFactory.getLogger(ProdAttributeController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getProdAttrById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String id) {
        try {
            logger.debug("Get product attribute with id: {}", id);
            return ResponseEntity.ok(productAttributeService.getById(id));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Collection<? extends BaseResponse>> getAllProdAttr() {
        logger.debug("Get all product attributes");
        return ResponseEntity.ok(productAttributeService.getAllProdAtt());
    }

    @GetMapping(path = "/cate")
    public ResponseEntity<Collection<? extends BaseResponse>> getAllProdAttrWithCategoryId(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String id) {
        logger.debug("Get all product attributes with category id: {}", id);
        return ResponseEntity.ok(productAttributeService.getAllProdAttWithCategoryId(id));
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewProdAttr(@Valid @RequestBody ProductAttributeDTO dto) {
        try {
            logger.debug("Create new product attribute");
            productAttributeService.createNewProdAtt(dto);
            return ResponseEntity.ok(new SimpleResponse("Create new product attribute successfully!"));
        } catch (ObjectAlreadyExistsException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<? extends BaseResponse> updateProdAttr(@Valid @RequestBody ProductAttributeDTO dto) {
        try {
            logger.debug("Update product attribute");
            return ResponseEntity.ok(productAttributeService.updateProdAtt(dto));
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteById(@RequestParam("id") @Pattern(regexp = ID_PATTERN, message = "ID is invalid") String id) {
        logger.debug("Delete product attribute with id: {}", id);
        productAttributeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
