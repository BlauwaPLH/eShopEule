package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.CartItemDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.CartException;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/cart")
public class CartController {

    private final CartService cartService;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping
    @Operation(description = "Get cart of current user (Customer)")
    public ResponseEntity<? extends BaseResponse> getCartOfCurrentUser() {
        logger.info("Get cart of current user");
        try {
            return ResponseEntity.ok(cartService.getCartOfCurrentUser());
        } catch (NotFoundException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping
    @Operation(description = "Update cart with a item")
    public ResponseEntity<? extends BaseResponse> updateCartItem(@Valid @RequestBody CartItemDTO dto) {
        logger.info("Update cart item");
        try {
            return ResponseEntity.ok(cartService.addToCart(dto));
        } catch (NotFoundException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping(path = "/lst")
    @Operation(description = "Update cart with item list")
    public ResponseEntity<? extends BaseResponse> updateCartItem(@Valid @RequestBody List<CartItemDTO> dtoList) {
        logger.info("Update cart item list");
        try {
            return ResponseEntity.ok(cartService.addToCart(dtoList));
        } catch (NotFoundException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del/prod")
    @Operation(description = "Remove cart item with product ID")
    public ResponseEntity<? extends BaseResponse> deleteCartItemWithProductId(@RequestParam("id") String productId) {
        logger.info("Remove cart item with product ID: {}", productId);
        try {
            cartService.deleteCartItemByProductId(productId);
            return ResponseEntity.ok(new SimpleResponse("Remove cart item successfully"));
        } catch (NotFoundException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del/opt")
    @Operation(description = "Remove cart item with option ID")
    public ResponseEntity<? extends BaseResponse> deleteCartItemWithOptionId(@RequestParam("id") String optionId) {
        logger.info("Remove cart item with option ID: {}", optionId);
        try {
            cartService.deleteCartItemByOptionId(optionId);
            return ResponseEntity.ok(new SimpleResponse("Remove cart item successfully"));
        } catch (NotFoundException | CartException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
