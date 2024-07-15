package org.senju.eshopeule.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
public final class CreateOrderRequest implements BaseRequest {

    @Serial
    private static final long serialVersionUID = 166558970012432816L;

    @NotBlank(message = "Delivery method must be required")
    @JsonProperty(value = "delivery_method")
    private String deliveryMethod;

    @NotBlank(message = "Transaction type must be required")
    @JsonProperty(value = "transaction_type")
    private String transactionType;
}
