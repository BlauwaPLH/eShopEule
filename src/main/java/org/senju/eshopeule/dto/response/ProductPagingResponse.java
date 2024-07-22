package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.senju.eshopeule.dto.ProductDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductPagingResponse extends PagingResponse {
    @Serial
    private static final long serialVersionUID = 211294473286646591L;

    private List<? extends ProductDTO> products;

    public ProductPagingResponse(Long totalElements, Integer totalPages, int pageNo, int pageSize, Boolean isLast, List<? extends ProductDTO> products) {
        super(totalElements, totalPages, pageNo, pageSize, isLast);
        this.products = products;
    }
}
