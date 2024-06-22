package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.model.user.Permission;

public final class PermissionMapper implements Mapper<Permission, PermissionDTO> {

    private static final PermissionMapper INSTANCE = new PermissionMapper();
    private PermissionMapper() {}

    public static PermissionMapper getInstance() {return INSTANCE;}

    @Override
    public Permission convertToEntity(PermissionDTO dto) {
        return Permission.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }

    @Override
    public PermissionDTO convertToDTO(Permission entity) {
        return PermissionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
