package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.BaseDTO;

@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PagingResponse implements BaseDTO {

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

}
