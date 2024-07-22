package org.senju.eshopeule.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.senju.eshopeule.dto.RatingDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RatingPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = 5021655509179084967L;

    private List<RatingDTO> ratings;

    public RatingPagingResponse(Long totalElements, Integer totalPages, int pageNo, int pageSize, Boolean isLast, List<RatingDTO> ratings) {
        super(totalElements, totalPages, pageNo, pageSize, isLast);
        this.ratings = ratings;
    }
}
