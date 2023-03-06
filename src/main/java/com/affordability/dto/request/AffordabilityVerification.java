package com.affordability.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AffordabilityVerification {

    @NotBlank(message = "agreement_number is mandatory")
    private String agreementNumber;

    @NotBlank(message = "transaction_activity is mandatory")
    @Pattern(regexp = "(?i)^(new_business|Renewal|MTA)$", message = "Invalid transaction_activity")
    private String transactionActivity;

    @NotBlank(message = "environment is mandatory")
    private String environment;
}
