package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.senju.eshopeule.dto.BaseDTO;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class PagingResponse implements BaseDTO {

    @JsonProperty(value = "total_elements")
    private Long totalElements;

    @JsonProperty(value = "total_pages")
    private Integer totalPages;

    @JsonProperty(value = "page_no")
    private int pageNo;

    @JsonProperty(value = "page_size")
    private int pageSize;

    @JsonProperty(value = "is_last")
    private Boolean isLast;

    public PagingResponse(int pageNo, int pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }
}