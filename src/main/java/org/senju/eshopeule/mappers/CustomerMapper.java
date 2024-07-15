package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.ProfileDTO;
import org.senju.eshopeule.model.user.Customer;
import org.senju.eshopeule.model.user.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CustomerMapper implements BaseMapper<Customer, ProfileDTO> {

    @Override
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "carts", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "gender", expression = "java(mappingGender(dto))")
    @Mapping(target = "birthDate", expression = "java(mappingBirthDate(dto))")
    public abstract Customer convertToEntity(ProfileDTO dto);

    @Override
    @Mapping(target = "fullName", expression = "java(entity.getFullName())")
    @Mapping(target = "gender", expression = "java(mappingGender(entity))")
    @Mapping(target = "birthDate", expression = "java(mappingBirthDate(entity))")
    public abstract ProfileDTO convertToDTO(Customer entity);

    protected Gender mappingGender(ProfileDTO dto) {
        if (dto.getGender() == null || dto.getGender().isBlank()) return null;
        Map<String, Gender> genderMap = Arrays.stream(Gender.values())
                .collect(Collectors.toMap(Gender::name, Function.identity()));
        if (genderMap.containsKey(dto.getGender())) return genderMap.get(dto.getGender());
        return null;
    }

    protected LocalDateTime mappingBirthDate(ProfileDTO dto) {
        if (dto.getBirthDate() == null) return null;
        return dto.getBirthDate().atStartOfDay();
    }

    protected String mappingGender(Customer entity) {
        if (entity.getGender() == null) return null;
        return entity.getGender().name();
    }

    protected LocalDate mappingBirthDate(Customer entity) {
        if (entity.getBirthDate() == null) return null;
        return entity.getBirthDate().toLocalDate();
    }
}
