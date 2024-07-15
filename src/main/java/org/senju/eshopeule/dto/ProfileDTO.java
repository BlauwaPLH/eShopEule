package org.senju.eshopeule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serial;
import java.time.LocalDate;

import static org.senju.eshopeule.constant.pattern.RegexPattern.PHONE_PATTERN;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProfileDTO implements BaseDTO {

    @Serial
    private static final long serialVersionUID = 1920098059254208841L;

    private String id;

    @NotBlank(message = "First name field must be required")
    @JsonProperty(value = "first_name")
    private String firstName;

    @NotBlank(message = "Last name field must be required")
    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "full_name")
    private String fullName;

    @JsonProperty(value = "birth_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private String gender;

    @JsonProperty(value = "phone_number")
    @NotBlank(message = "Phone number field must be required")
    @Pattern(regexp = PHONE_PATTERN, message = "Phone number is invalid")
    private String phoneNumber;

    @NotBlank(message = "Address field must be required")
    private String address;
}
