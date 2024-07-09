package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ProductException;
import org.senju.eshopeule.service.ProductImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/img")
public class ProdImageController {

    private final ProductImageService productImageService;
    private static final Logger logger = LoggerFactory.getLogger(ProdImageController.class);

    @GetMapping
    public ResponseEntity<?> getImageUrlById(@RequestParam("id") String id) {
        logger.info("Get product image url with id: {}", id);
        try {
            return ResponseEntity.ok(productImageService.getImageUrlById(id));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/prod")
    public ResponseEntity<?> getAllImageUrlByProductId(@RequestParam("id") String productId) {
        logger.info("Get all product image url with product id: {}", productId);
        return ResponseEntity.ok(productImageService.getAllImageUrlByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<?> uploadProductImages(
            @RequestParam("file") MultipartFile[] files,
            @RequestParam("id") String productId) {
        logger.info("Upload product images");
        try {
            productImageService.uploadImage(files, productId);
            return ResponseEntity.ok(new SimpleResponse("Upload product images successfully!"));
        } catch (NotFoundException | ProductException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteById(@RequestParam("id") String id) {
        logger.info("Delete product image with id: {}", id);
        try {
            productImageService.deleteById(id);
            return ResponseEntity.ok(new SimpleResponse("Delete product image successfully!"));
        } catch (NotFoundException | ProductException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/prod/del")
    public ResponseEntity<?> deleteByProductId(@RequestParam("id") String productId) {
        logger.info("Delete product image with product id: {}", productId);
        try {
            productImageService.deleteByProductId(productId);
            return ResponseEntity.ok(new SimpleResponse("Delete product image successfully!"));
        } catch (NotFoundException | ProductException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
