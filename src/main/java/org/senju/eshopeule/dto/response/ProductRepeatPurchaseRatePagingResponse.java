package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.ProdRepeatPurchaseRateDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductRepeatPurchaseRatePagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = 1677914481017576108L;

    private List<ProdRepeatPurchaseRateDTO> contents;

    public ProductRepeatPurchaseRatePagingResponse(int pageNo, int pageSize, List<ProdRepeatPurchaseRateDTO> contents) {
        super(pageNo, pageSize);
        this.contents = contents;
    }
}
