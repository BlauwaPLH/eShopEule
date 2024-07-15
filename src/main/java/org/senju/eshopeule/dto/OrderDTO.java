package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class OrderDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = -6357570630532078595L;

    private String id;

    @JsonProperty(value = "contact_name")
    private String contactName;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    private String address;

    private Double total;

    @JsonProperty(value = "delivery_method")
    private String deliveryMethod;

    private String status;

    @JsonProperty(value = "transaction_type")
    private String transactionType;

    private List<OrderItemDTO> items;
}
