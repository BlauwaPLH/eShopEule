package org.senju.eshopeule.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.exceptions.ObjectAlreadyExistsException;
import org.senju.eshopeule.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/staff")
public class StaffController {

    private final StaffService staffService;

    private final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @GetMapping(path = "/all")
    @Operation(summary = "Get all staff")
    public ResponseEntity<Collection<StaffDTO>> getAllStaff() {
        logger.info("Get all staff");
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping
    @Operation(summary = "Get staff with ID")
    public ResponseEntity<? extends BaseResponse> getStaffWithId(@RequestParam("id") String id) {
        logger.info("Get staff with id: {}", id);
        try {
            return ResponseEntity.ok(staffService.getStaffWithId(id));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PostMapping
    @Operation(summary = "Create new staff")
    public ResponseEntity<? extends BaseResponse> createNewStaff(@Valid @RequestBody StaffDTO staffDTO) {
        logger.info("Create new staff account");
        try {
            staffService.createAccount(staffDTO);
            return ResponseEntity.ok(new SimpleResponse("Create staff account successfully"));
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @PutMapping()
    @Operation(summary = "Update staff")
    public ResponseEntity<? extends BaseResponse> updateStaffAccount(@Valid @RequestBody StaffDTO staffDTO) {
        logger.info("Update staff account");
        try {
            return ResponseEntity.ok(staffService.updateAccount(staffDTO));
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del/{id}")
    @Operation(summary = "Delete staff with id")
    public ResponseEntity<?> deleteStaffWithId(@PathVariable("id") String id) {
        staffService.deleteStaffWithId(id);
        return ResponseEntity.noContent().build();
    }
}
