package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.exceptions.PermissionNotExistsException;
import org.senju.eshopeule.model.user.Permission;

public interface PermissionService {
    PermissionDTO getDTOByName(String name) throws PermissionNotExistsException;

    Permission getByName(String name) throws PermissionNotExistsException;

    Permission bootstrapPerm(String name);
}
