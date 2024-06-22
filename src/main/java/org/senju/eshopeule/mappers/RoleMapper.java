package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.model.user.Role;

public final class RoleMapper implements Mapper<Role, RoleDTO> {

    private static final PermissionMapper permMapper = PermissionMapper.getInstance();
    private static final RoleMapper INSTANCE = new RoleMapper();
    private RoleMapper() {}

    public static RoleMapper getInstance() {return INSTANCE;}

    @Override
    public Role convertToEntity(RoleDTO dto) {
        return Role.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .permissions(dto.getPermissions().stream().map(permMapper::convertToEntity).toList())
                .build();
    }

    @Override
    public RoleDTO convertToDTO(Role entity) {
        return RoleDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .permissions(entity.getPermissions().stream().map(permMapper::convertToDTO).toList())
                .build();
    }
}
