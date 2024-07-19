package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.OrderedProductStatDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderedProductStatPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = -916742391291981587L;

    private List<OrderedProductStatDTO> contents;

    public OrderedProductStatPagingResponse(int pageNo, int pageSize, List<OrderedProductStatDTO> contents) {
        super(pageNo, pageSize);
        this.contents = contents;
    }
}
