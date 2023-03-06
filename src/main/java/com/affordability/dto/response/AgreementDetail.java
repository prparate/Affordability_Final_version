package com.affordability.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgreementDetail {
    private Integer agrSerial;
    private String agrAgreementNumber;
    private String agrCreateDate;
    private String agrRenewedAgreement;
    private BigDecimal agrAdvance;
    private BigDecimal agrInstalment;
    private String agrStatus1;
    private BigDecimal agrBalance;
    private BigDecimal agrArrears;
    private BigDecimal agrIndirectDeposit;
    private Integer hostCompanySerial;
    private String hostCompanyFlag;
    private Integer thpSerial;
    private String thpIdNumber;
    private String thpTradingName;
    private String personSerial;
    private String personNameFirst;
    private String personNameMiddle;
    private String personNameLast;
    private String personNameFull;
    private String personDateOfBirth;
    private String personMobilePhone;
    private String personEmail;
    private String personTitle;
    private String bankAccountNumber;
    private String bankAccountSortCode;
    private String personHabitationDateIn;
    private String personHabitationDateOut;
    private String personCountry;
    private String personCountyCode;
    private String personHouseName;
    private String personHouseNumber;
    private String personLocality;
    private String personPostCode;
    private String personPostTown;
    private String personStreetName;
    private String personStreetType;
    private Integer companySerial;
    private String companyName;
    private String companyRegistrationNumber;
    private String companyBusinessType;
    private String companyDateEstablished;
    private String companyTelephone;
    private String companyEmail;
    private String companyVlcCode;
    private String companyHabitationDateIn;
    private String companyHabitationDateOut;
    private String companyCountry;
    private String companyCountyCode;
    private String companyHouseName;
    private String companyHouseNumber;
    private String companyLocality;
    private String companyPostCode;
    private String companyPostTown;
    private String companyStreetName;
    private String companyStreetType;
    private String secReference;
    private Integer cancellationTypeSerial;
    private String cancellationTypeCode;
    private Integer insurerSerial;
    private BigDecimal mtaIncreases;
    private BigDecimal totalUnpaids;
    private String currentTimestamp;
    private BigDecimal agrAdvancePlusIndirectDeposit;

}
