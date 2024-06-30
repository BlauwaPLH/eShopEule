package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.senju.eshopeule.dto.PermissionDTO;
import org.senju.eshopeule.model.user.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends BaseMapper<Permission, PermissionDTO> {

    @Override
    @Mapping(target = "roles", ignore = true)
    Permission convertToEntity(PermissionDTO dto);


}
