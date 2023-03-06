package com.affordability.dto.response;

import com.affordability.model.EStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AgreementLastRecordResponse {
    private String agreementNumber;
    private String environment;
    private EStatus status;

    public AgreementLastRecordResponse(String agreementNumber, String environment, String status) {
        this.agreementNumber = agreementNumber;
        this.environment = environment;
        this.status = EStatus.valueOf(status);
    }
}
