package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.exceptions.UserAlreadyExistsException;
import org.senju.eshopeule.exceptions.UserNotExistsException;
import org.senju.eshopeule.service.StaffService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/staff")
public class StaffController {

    private final StaffService staffService;

    private final Logger logger = LoggerFactory.getLogger(StaffController.class);

    @GetMapping(path = "/all")
    public ResponseEntity<?> getAllStaff() {
        logger.debug("Get all staff");
        return ResponseEntity.ok(staffService.getAllStaff());
    }

    @GetMapping
    public ResponseEntity<?> getStaffWithId(@RequestParam("id") String id) {
        logger.debug("Get staff with id: {}", id);
        try {
            return ResponseEntity.ok(staffService.getStaffWithId(id));
        } catch (UserNotExistsException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewStaff(@Valid @RequestBody StaffDTO staffDTO) {
        logger.debug("Create new staff account");
        try {
            staffService.createAccount(staffDTO);
            return ResponseEntity.ok(Collections.singletonMap("message", "Create staff account successfully"));
        } catch (UserAlreadyExistsException | RoleNotExistsException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateStaffAccount(@Valid @RequestBody StaffDTO staffDTO) {
        logger.debug("Update staff account");
        try {
            return ResponseEntity.ok(staffService.updateAccount(staffDTO));
        } catch (UserAlreadyExistsException | UserNotExistsException | RoleNotExistsException ex) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del/{id}")
    public ResponseEntity<?> deleteStaffWithId(@PathVariable("id") String id) {
        staffService.deleteStaffWithId(id);
        return ResponseEntity.noContent().build();
    }
}
