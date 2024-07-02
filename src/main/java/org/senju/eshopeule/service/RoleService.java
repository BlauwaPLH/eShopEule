package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.exceptions.NotFoundException;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    RoleDTO getById(String id) throws NotFoundException;

    void createNewRole(RoleDTO role);

    RoleDTO updateRole(RoleDTO role) throws NotFoundException;

    void deleteById(String id);

    List<RoleDTO> getAllRole();

    List<RoleDTO> getAllStaffRole();

    Role bootstrapRole(String name, Collection<Permission> permissions);
}
