package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.dto.response.BaseResponse;
import org.senju.eshopeule.dto.response.SimpleResponse;
import org.senju.eshopeule.exceptions.PermissionNotExistsException;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.service.PermissionService;
import org.senju.eshopeule.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/role")
public class RoleController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping
    public ResponseEntity<? extends BaseResponse> getRoleById(@RequestParam("id") String id) {
        logger.debug("Get role with id: {}", id);
        try {
            return ResponseEntity.ok(roleService.getById(id));
        } catch (RoleNotExistsException ex) {
            return ResponseEntity.status(NOT_FOUND).body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<Collection<RoleDTO>> getAllRoles() {
        logger.debug("Get all roles");
        return ResponseEntity.ok(roleService.getAllRole());
    }

    @GetMapping(path = "/staff")
    public ResponseEntity<Collection<RoleDTO>> getAllStaffRole() {
        logger.debug("Get all staff role");
        return ResponseEntity.ok(roleService.getAllStaffRole());
    }


    @PostMapping
    public ResponseEntity<? extends BaseResponse> createNewRole(@RequestBody RoleDTO role) {
        logger.debug("Create new role");
        roleService.createNewRole(role);
        return ResponseEntity.ok(SimpleResponse.builder().message("Save successfully").build());
    }

    @PutMapping
    public ResponseEntity<? extends BaseResponse> updateRole(@RequestParam("id") String id, @RequestBody RoleDTO role) {
        logger.debug("Update role");
        try {
            return ResponseEntity.ok(roleService.updateRole(role));
        } catch (RoleNotExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }

    @DeleteMapping(path = "/del/{id}")
    public ResponseEntity<?> deleteRoleById(@PathVariable("id") String id) {
        logger.debug("Delete role with id: {}", id);
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/perm/all")
    public ResponseEntity<Collection<PermissionDTO>> getAllPermission() {
        logger.debug("Get all permissions");
        return ResponseEntity.ok(permissionService.getAllPermission());
    }

    @GetMapping(path = "/perm")
    public ResponseEntity<? extends BaseResponse> getPermissionById(@RequestParam("id") String id) {
        logger.debug("Get permission with id: {}", id);
        try {
            return ResponseEntity.ok(permissionService.getById(id));
        } catch (PermissionNotExistsException ex) {
            return ResponseEntity.status(NOT_FOUND).body(SimpleResponse.builder().message(ex.getMessage()).build());
        }
    }
}
