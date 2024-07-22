package org.senju.eshopeule.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.senju.eshopeule.dto.CategoryOrderedStatDTO;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryOrderedStatPagingResponse extends PagingResponse {

    @Serial
    private static final long serialVersionUID = -5511985092338442781L;

    private List<CategoryOrderedStatDTO> contents;

    public CategoryOrderedStatPagingResponse(int pageNo, int pageSize, List<CategoryOrderedStatDTO> contents) {
        super(pageNo, pageSize);
        this.contents = contents;
    }
}
