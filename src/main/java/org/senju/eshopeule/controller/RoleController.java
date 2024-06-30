package org.senju.eshopeule.controller;

import lombok.RequiredArgsConstructor;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.exceptions.PermissionNotExistsException;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.service.PermissionService;
import org.senju.eshopeule.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/p/v1/role")
public class RoleController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @GetMapping
    public ResponseEntity<?> getRoleById(@RequestParam("id") String id) {
        logger.debug("Get role with id: {}", id);
        try {
            return ResponseEntity.ok(roleService.getById(id));
        } catch (RoleNotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @GetMapping(path = "/all")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        logger.debug("Get all roles");
        return ResponseEntity.ok(roleService.getAllRole());
    }

    @PostMapping
    public ResponseEntity<?> createNewRole(@RequestBody RoleDTO role) {
        logger.debug("Create new role");
        roleService.createNewRole(role);
        return ResponseEntity.ok(Collections.singletonMap("message", "Save successfully"));
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@RequestParam("id") String id, @RequestBody RoleDTO role) {
        logger.debug("Update role");
        try {
            return ResponseEntity.ok(roleService.updateRole(role));
        } catch (RoleNotExistsException ex) {
            logger.error(ex.getMessage());
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @DeleteMapping(path = "/del")
    public ResponseEntity<?> deleteRoleById(@RequestParam("id") String id) {
        logger.debug("Delete role with id: {}", id);
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/perm/all")
    public ResponseEntity<List<PermissionDTO>> getAllPermission() {
        logger.debug("Get all permissions");
        return ResponseEntity.ok(permissionService.getAllPermission());
    }

    @GetMapping(path = "/perm")
    public ResponseEntity<?> getPermissionById(@RequestParam("id") String id) {
        logger.debug("Get permission with id: {}", id);
        try {
            return ResponseEntity.ok(permissionService.getById(id));
        } catch (PermissionNotExistsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        }
    }
}
