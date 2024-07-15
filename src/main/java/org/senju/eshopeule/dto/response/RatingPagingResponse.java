package org.senju.eshopeule.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.senju.eshopeule.dto.RatingDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RatingPagingResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = 5021655509179084967L;

    @JsonProperty(value = "total_elements")
    private long totalElements;

    @JsonProperty(value = "total_pages")
    private int totalPages;

    @JsonProperty(value = "page_no")
    private int pageNo;

    @JsonProperty(value = "page_size")
    private int pageSize;

    @JsonProperty(value = "is_last")
    private boolean isLast;

    private List<RatingDTO> ratings;
}
