package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.ProductDTO;

import java.io.Serial;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductSearchResultResponse extends PagingResponse {
    @Serial
    private static final long serialVersionUID = 4078325868239951606L;

    private List<? extends ProductDTO> products;

    private Map<String, Map<String, Long>> aggregations;

    public ProductSearchResultResponse(long totalElements, int totalPages, int pageNo,
                                       int pageSize, boolean isLast,
                                       List<? extends ProductDTO> products,
                                       Map<String, Map<String, Long>> aggregations) {
        super(totalElements, totalPages, pageNo, pageSize, isLast);
        this.aggregations = aggregations;
        this.products = products;
    }
}
