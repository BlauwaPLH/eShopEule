package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.ProfileDTO;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.model.user.Gender;

import static org.senju.eshopeule.model.user.Gender.FEMALE;
import static org.senju.eshopeule.model.user.Gender.MALE;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper implements BaseMapper<Customer, ProfileDTO> {

    @Override
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "gender", expression = "java(mappingGender(dto))")
    public abstract Customer convertToEntity(ProfileDTO dto);

    @Override
    @Mapping(target = "fullName", expression = "java(entity.getFullName())")
    @Mapping(target = "gender", expression = "java(entity.getGender().name())")
    public abstract ProfileDTO convertToDTO(Customer entity);

    protected Gender mappingGender(ProfileDTO dto) {
        if (dto.getGender() == null) return null;
        switch (Gender.valueOf(dto.getGender())) {
            case MALE -> {return MALE;}
            case FEMALE -> {return FEMALE;}
            default -> {return null;}
        }
    }
}
