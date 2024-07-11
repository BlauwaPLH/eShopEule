package org.senju.eshopeule.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.service.PermissionService;
import org.senju.eshopeule.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/r/v1/role")
public class RoleController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getRoleById(@RequestParam("id") String id) {
        logger.info("Get role with id: {}", id);
        try {
            return ResponseEntity.ok(roleService.getById(id));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(new SimpleResponse(ex.getMessage()));
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Collection<RoleDTO>> getAllRoles() {
        logger.info("Get all roles");
        return ResponseEntity.ok(roleService.getAllRole());
    }

    @GetMapping(path = "/staff")
    public ResponseEntity<Collection<RoleDTO>> getAllStaffRole() {
        logger.info("Get all staff role");
        return ResponseEntity.ok(roleService.getAllStaffRole());
    }


    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewRole(@Valid  @RequestBody RoleDTO role) {
        logger.info("Create new role");
        roleService.createNewRole(role);
        return ResponseEntity.ok(new SimpleResponse("Save successfully"));
    }

    @PutMapping
    public ResponseEntity<? extends BaseResponse> updateRole(@Valid @RequestBody RoleDTO role) {
        logger.info("Update role");
        try {
            return ResponseEntity.ok(roleService.updateRole(role));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(new SimpleResponse(ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("id") String id) {
        logger.info("Delete role with id: {}", id);
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/perm/all")
    public ResponseEntity<Collection<PermissionDTO>> getAllPermission() {
        logger.info("Get all permissions");
        return ResponseEntity.ok(permissionService.getAllPermission());
    }

    @GetMapping(path = "/perm")
    public ResponseEntity<? extends BaseResponse> getPermissionById(@Valid @RequestParam("id") String id) {
        logger.info("Get permission with id: {}", id);
        try {
            return ResponseEntity.ok(permissionService.getById(id));
        } catch (NotFoundException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.status(NOT_FOUND).body(new SimpleResponse(ex.getMessage()));
        }
    }
}
