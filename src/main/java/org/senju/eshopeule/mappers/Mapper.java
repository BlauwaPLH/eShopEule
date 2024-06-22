package org.senju.eshopeule.mappers;

import org.senju.eshopeule.dto.BaseDTO;
import org.senju.eshopeule.model.BaseEntity;

public interface Mapper<E extends BaseEntity, D extends BaseDTO> {

    E convertToEntity(D dto);

    D convertToDTO(E entity);
}
