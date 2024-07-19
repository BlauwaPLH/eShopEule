package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.BrandStatDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class BrandStatPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = 4230350467046053699L;

    private List<BrandStatDTO> contents;

    public BrandStatPagingResponse(int pageNo, int pageSize, List<BrandStatDTO> contents) {
        super(pageNo, pageSize);
        this.contents = contents;
    }
}
