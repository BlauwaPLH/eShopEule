package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serial;
import java.time.LocalDate;

@Getter
public class QueryByDateRangeRequest implements BaseRequest {
    @Serial
    private static final long serialVersionUID = -2646838517849272388L;

    @NotBlank(message = "Start date must be required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "start_date")
    private LocalDate from;

    @NotBlank(message = "End date must be required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "end_date")
    private LocalDate to;
}
