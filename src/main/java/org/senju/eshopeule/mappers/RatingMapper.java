package org.senju.eshopeule.mappers;

import org.mapstruct.*;
import org.senju.eshopeule.dto.RatingDTO;
import org.senju.eshopeule.model.rating.Rating;

@Mapper(componentModel = "spring")
public interface RatingMapper extends BaseMapper<Rating, RatingDTO> {

    @Override
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "customerName", ignore = true)
    Rating convertToEntity(RatingDTO dto);

    @Override
    RatingDTO convertToDTO(Rating entity);

    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "customerName", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lastModifiedOn", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDTO(RatingDTO dto, @MappingTarget Rating entity);
}
