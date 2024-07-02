package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.model.user.Permission;

import java.util.List;

public interface PermissionService {
    PermissionDTO getById(String id) throws NotFoundException;

    List<PermissionDTO> getAllPermission();

    Permission bootstrapPerm(String name);
}
