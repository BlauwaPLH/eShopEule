package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.model.user.Permission;
import org.senju.eshopeule.model.user.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDTO> {

    @Override
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "permissions", expression = "java(mapPermissions(dto))")
    Role convertToEntity(RoleDTO dto);

    default List<Permission> mapPermissions(RoleDTO dto) {
        if (dto.getPermissions() == null) return null;
        return dto.getPermissions().stream()
                .map(src -> Permission.builder()
                        .id(src.getId())
                        .name(src.getName())
                        .build())
                .collect(Collectors.toList());
    }

    default Role updateFromDto(RoleDTO dto, Role role) {
        if (dto.getName() != null) role.setName(dto.getName());
        if (dto.getDescription() != null) role.setDescription(dto.getDescription());
        role.setPermissions(mapPermissions(dto));
        return role;
    }
}
