package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
public final class ProductOrderStatusStatRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = -7530088001709194289L;

    @NotBlank(message = "Product ID must be not blank")
    @JsonProperty(value = "product_id")
    private String productId;

    @JsonProperty(value = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @JsonProperty(value = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;
}
