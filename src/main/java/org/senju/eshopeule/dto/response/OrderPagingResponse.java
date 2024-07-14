package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.senju.eshopeule.dto.OrderDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderPagingResponse implements BaseResponse {

    @Serial
    private static final long serialVersionUID = 947042839835701175L;

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

    private List<OrderDTO> orders;
}
