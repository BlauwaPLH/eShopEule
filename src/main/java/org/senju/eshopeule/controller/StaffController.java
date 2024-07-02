package org.senju.eshopeule.controller;

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
@RequestMapping(path = "/api/p/v1/staff")
public class StaffController {

    private final StaffService staffService;

    private final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @GetMapping(path = "/all")
    public ResponseEntity<Collection<StaffDTO>> getAllStaff() {
        logger.debug("Get all staff");
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getStaffWithId(@RequestParam("id") String id) {
        logger.debug("Get staff with id: {}", id);
        try {
            return ResponseEntity.ok(staffService.getStaffWithId(id));
        } catch (NotFoundException ex) {
            return ResponseEntity.badRequest().body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }

    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewStaff(@Valid @RequestBody StaffDTO staffDTO) {
        logger.debug("Create new staff account");
        try {
            staffService.createAccount(staffDTO);
            return ResponseEntity.ok(SimpleResponse.builder().message("Create staff account successfully").build());
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }

    @PutMapping()
    public ResponseEntity<? extends BaseResponse> updateStaffAccount(@Valid @RequestBody StaffDTO staffDTO) {
        logger.debug("Update staff account");
        try {
            return ResponseEntity.ok(staffService.updateAccount(staffDTO));
        } catch (ObjectAlreadyExistsException | NotFoundException ex) {
            return ResponseEntity.badRequest().body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }

    @DeleteMapping(path = "/del/{id}")
    public ResponseEntity<?> deleteStaffWithId(@PathVariable("id") String id) {
        staffService.deleteStaffWithId(id);
        return ResponseEntity.noContent().build();
    }
}
