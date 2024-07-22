package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.senju.eshopeule.dto.OrderDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class OrderPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = 947042839835701175L;

    private List<OrderDTO> orders;

    public OrderPagingResponse(Long totalElements, Integer totalPages, int pageNo, int pageSize, Boolean isLast, List<OrderDTO> orders) {
        super(totalElements, totalPages, pageNo, pageSize, isLast);
        this.orders = orders;
    }
}
