package org.senju.eshopeule.service;

import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.exceptions.RoleNotExistsException;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;

import java.util.Collection;
import java.util.List;

public interface RoleService {
    RoleDTO findByName(String name) throws RoleNotExistsException;

    RoleDTO save(RoleDTO role);

    void deleteById(String id);

    Role bootstrapRole(String name, Collection<Permission> permissions);
}
