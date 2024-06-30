package org.senju.eshopeule.mappers;

import org.mapstruct.Mapper;
import org.senju.eshopeule.dto.StaffDTO;
import org.senju.eshopeule.model.user.User;

@Mapper(componentModel = "spring")
public interface StaffMapper extends BaseMapper<User, StaffDTO> {

    @Override
    User convertToEntity(StaffDTO dto);

    @Override
    StaffDTO convertToDTO(User entity);
}
