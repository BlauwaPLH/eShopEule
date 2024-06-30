package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.RoleDTO;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.model.user.Role;
import org.senju.eshopeule.model.user.User;

@Mapper(componentModel = "spring")
public interface StaffMapper extends BaseMapper<User, StaffDTO> {

    @Override
    @Mapping(target = "isAccountNonExpired", ignore = true)
    @Mapping(target = "isAccountNonLocked", ignore = true)
    @Mapping(target = "isCredentialsNonExpired", ignore = true)
    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "role", expression = "java(mapRoleFromDTO(dto))")
    User convertToEntity(StaffDTO dto);

    default Role mapRoleFromDTO(StaffDTO dto) {
        if (dto.getRole() == null) return null;
        return Role.builder()
                .id(dto.getRole().getId())
                .name(dto.getRole().getName())
                .build();
    }

    default User updateFromDTO(StaffDTO dto, User entity) {
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getEmail() != null) entity.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEnabled(dto.isEnabled());
        entity.setRole(mapRoleFromDTO(dto));
        return entity;
    }


    @Override
    @Mapping(target = "enabled", source = "enabled")
    @Mapping(target = "password", ignore = true)
    StaffDTO convertToDTO(User entity);

    default RoleDTO roleToRoleDTO(Role role) {
        if (role == null) return null;
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
