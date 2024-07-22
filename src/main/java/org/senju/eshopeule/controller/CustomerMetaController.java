package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.ProfileDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/cm")
public class CustomerMetaController {

    private final CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerMetaController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getMyProfile() {
        logger.info("Get profile of current user (customer)");
        try {
            return ResponseEntity.ok(customerService.getProfileOfCurrentUser());
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> updateProfile(@Valid @RequestBody ProfileDTO dto) {
        logger.info("Update profile customer");
        try {
            return ResponseEntity.ok(customerService.updateProfile(dto));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }
}
