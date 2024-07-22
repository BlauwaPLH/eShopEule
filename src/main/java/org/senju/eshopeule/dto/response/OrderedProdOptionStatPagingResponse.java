package org.senju.eshopeule.dto.response;

import lombok.Getter;
import org.senju.eshopeule.dto.OrderedProdOptionStatDTO;

import java.io.Serial;
import java.util.List;

@Getter
public class OrderedProdOptionStatPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = -7465650816442180737L;

    private List<OrderedProdOptionStatDTO> contents;

    public OrderedProdOptionStatPagingResponse(int pageNo, int pageSize, List<OrderedProdOptionStatDTO> contents) {
        super(pageNo, pageSize);
        this.contents = contents;
    }
}
