package com.affordability.model;

import com.affordability.customer.ETraderType;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementQueryResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static com.affordability.model.EBusinessLine.COMMERCIAL;
import static com.affordability.model.EBusinessLine.PERSONAL;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

class AffordabilitySummaryTest {

    @Test
    @DisplayName("Should not throw error when try to confirm outcome with null values")
    void shouldNotThrowErrorWhenTryToConfirmOutComeWithNullValues() {
        var domainModel = new AffordabilitySummary(null, null, null);

        assertAll(
            () -> assertThatNoException().isThrownBy(
                () -> domainModel.confirmOutcome(null, null, null, null, null)
            ),

            () -> assertNotNull(domainModel.customer),
            () -> assertNotNull(domainModel.createdOn),

            () -> assertNull(domainModel.agreementNumber),
            () -> assertNull(domainModel.environment),
            () -> assertNull(domainModel.customerSegment),
            () -> assertNull(domainModel.affordabilityDelphi),
            () -> assertNull(domainModel.consumerSummaryDelphi),
            () -> assertEquals(EStatus.NOT_ON_SCHEME, domainModel.status),
            () -> assertNull(domainModel.exclusionReason),
            () -> assertFalse(domainModel.isFunded),
            () -> assertNull(domainModel.totalPremiumValue),
            () -> assertNull(domainModel.effectiveDisposableSurplus)
        );


    }


    @Test
    @DisplayName("Should not throw error when try to confirm outcome with Personal values")
    void shouldNotThrowErrorWhenTryToConfirmOutComeWithPersonalValues() {


        var agreementDetails = new AgreementDetail();
        agreementDetails.setPersonSerial("00001");
        agreementDetails.setPersonTitle("title");
        agreementDetails.setPersonNameFirst("first name");
        agreementDetails.setPersonNameLast("last name");
        agreementDetails.setPersonNameMiddle("middle name");
        agreementDetails.setPersonDateOfBirth("2000-01-01 00:00:00");
        agreementDetails.setPersonHouseName("house name");
        agreementDetails.setPersonStreetName("street name");
        agreementDetails.setPersonPostCode("0000-000");
        agreementDetails.setPersonPostTown("post town");
        agreementDetails.setPersonHouseNumber("house number");
        agreementDetails.setAgrAgreementNumber("312312311");
        agreementDetails.setPersonCountry("country");
        agreementDetails.setAgrAdvance(BigDecimal.valueOf(10));

        var domainModel = new AffordabilitySummary("000001", EActivityType.NEW, "TEST");
        var data = new AffordabilityDelphiDataResponse();
        var uuid = UUID.randomUUID();
        data.setUuid(uuid.toString());
        domainModel.addAgreementDetails(agreementDetails);
        domainModel.addPersonalInfo(agreementDetails, ETraderType.SOLE_TRADER);

        var delphiresponse = DelphiResponse.builder()
            .datetimeRequest("2022-01-01 00:00:00")
            .uuidInstance(UUID.randomUUID().toString())
            .data(data)
            .build();
        domainModel.addDelphiData(delphiresponse);

        assertAll(
            () -> assertThatNoException()
                .isThrownBy(() -> domainModel.confirmOutcome(null, null, null, null, null)),

            () -> assertEquals("000001", domainModel.agreementNumber),
            () -> assertEquals("TEST", domainModel.environment),
            () -> assertNotNull(domainModel.createdOn),

            () -> assertNotNull(domainModel.customer),
            () -> assertTrue(domainModel.customer.isNew),
            () -> assertEquals(PERSONAL, domainModel.customer.businessLine),


            () -> assertEquals("first name", domainModel.customer.forename),
            () -> assertEquals("middle name", domainModel.customer.middleName),
            () -> assertEquals("last name", domainModel.customer.surname),
            () -> assertEquals("title", domainModel.customer.title),
            () -> assertEquals(LocalDateTime
                    .of(2000, 1, 1, 0, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.customer.dateOfBirth),

            () -> assertNotNull(domainModel.customer.address),
            () -> assertEquals("country", domainModel.customer.address.country),
            () -> assertEquals("street name", domainModel.customer.address.street),
            () -> assertEquals("post town", domainModel.customer.address.postTown),
            () -> assertEquals("0000-000", domainModel.customer.address.postcode),
            () -> assertEquals("house name", domainModel.customer.address.houseName),
            () -> assertEquals("house number", domainModel.customer.address.houseNumber),


            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(uuid.toString(), domainModel.affordabilityDelphi.uuidInstance),
            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(LocalDateTime
                    .of(2022, 1, 1, 0, 0, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.affordabilityDelphi.requestedOn),

            () -> assertEquals(BigDecimal.valueOf(10), domainModel.totalPremiumValue),
            () -> assertNull(domainModel.customerSegment),
            () -> assertNull(domainModel.consumerSummaryDelphi),
            () -> assertEquals(EStatus.NOT_ON_SCHEME, domainModel.status),
            () -> assertNull(domainModel.exclusionReason),
            () -> assertFalse(domainModel.isFunded),
            () -> assertNull(domainModel.effectiveDisposableSurplus)
        );


    }


    @Test
    @DisplayName("Should not throw error when try to confirm outcome with company values")
    void shouldNotThrowErrorWhenTryToConfirmOutComeWithCompanyValues() {

        var agreementDetails = new AgreementDetail();
        agreementDetails.setCompanySerial(1);
        agreementDetails.setCompanyName("title");
        agreementDetails.setCompanyRegistrationNumber("house name");
        agreementDetails.setCompanyBusinessType("trader type");
        agreementDetails.setCompanyStreetName("street name");
        agreementDetails.setCompanyPostCode("0000-000");
        agreementDetails.setCompanyPostTown("post town");
        agreementDetails.setCompanyHouseNumber("house number");
        agreementDetails.setCompanyHouseName("house name");
        agreementDetails.setCompanyCountry("country");
        agreementDetails.setAgrAdvance(BigDecimal.valueOf(10));

        var domainModel = new AffordabilitySummary("000001", EActivityType.NEW, "TEST");
        domainModel.addAgreementDetails(agreementDetails);
        var partner = PartnerDetailsForCommercialAgreementQueryResponse.builder()
            .dateOfBirth("2000-01-01 23:00:00")
            .personMiddleName("test")
            .personForenames("test")
            .personSurname("test")
            .houseName("House")
            .houseNumber("number")
            .build();
        domainModel.addCommercialInfo(agreementDetails, partner, ETraderType.LIMITED_COMPANY);
        var uuid = UUID.randomUUID();
        var data = new AffordabilityDelphiDataResponse();
        data.setUuid(uuid.toString());
        var delphiresponse = DelphiResponse.builder()
            .datetimeRequest("2022-01-01 00:00:00")
            .uuidInstance(UUID.randomUUID().toString())
            .data(data)
            .build();
        domainModel.addDelphiData(delphiresponse);
        assertAll(
            () -> assertThatNoException()
                .isThrownBy(() -> domainModel.confirmOutcome(null, null, null, null, null)),

            () -> assertEquals("000001", domainModel.agreementNumber),
            () -> assertEquals("TEST", domainModel.environment),
            () -> assertNotNull(domainModel.createdOn),

            () -> assertNotNull(domainModel.customer),
            () -> assertTrue(domainModel.customer.isNew),
            () -> assertEquals(COMMERCIAL, domainModel.customer.businessLine),

            () -> assertEquals("title", domainModel.customer.companyName),
            () -> assertEquals("house name", domainModel.customer.companyNumber),
            () -> assertEquals(ETraderType.LIMITED_COMPANY, domainModel.customer.traderType),


            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(uuid.toString(), domainModel.affordabilityDelphi.uuidInstance),
            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(LocalDateTime
                    .of(2022, 1, 1, 0, 0, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.affordabilityDelphi.getRequestedOn()),

            () -> assertEquals(BigDecimal.valueOf(10), domainModel.totalPremiumValue),
            () -> assertNull(domainModel.customerSegment),
            () -> assertNull(domainModel.consumerSummaryDelphi),
            () -> assertEquals(EStatus.NOT_ON_SCHEME, domainModel.status),
            () -> assertNull(domainModel.exclusionReason),
            () -> assertFalse(domainModel.isFunded),
            () -> assertNull(domainModel.effectiveDisposableSurplus)
        );
    }


    @Test
    @DisplayName("Should return status On Scheme when person is under age")
    void shouldReturnStatusOnSchemeWhenPersonIsUnderAge() {

        var agreementDetails = new AgreementDetail();
        agreementDetails.setPersonSerial("00001");
        agreementDetails.setPersonTitle("title");
        agreementDetails.setPersonNameFirst("first name");
        agreementDetails.setPersonNameLast("last name");
        agreementDetails.setPersonNameMiddle("middle name");

        var dateOfBirth = LocalDateTime.now()
            .minusYears(17)
            .withNano(0);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        agreementDetails.setPersonDateOfBirth(dateOfBirth.format(formatter));
        agreementDetails.setPersonHouseName("house name");
        agreementDetails.setPersonStreetName("street name");
        agreementDetails.setPersonPostCode("0000-000");
        agreementDetails.setPersonPostTown("post town");
        agreementDetails.setPersonHouseNumber("house number");
        agreementDetails.setAgrAgreementNumber("312312311");
        agreementDetails.setPersonCountry("country");
        agreementDetails.setAgrAdvance(BigDecimal.valueOf(10));

        var domainModel = new AffordabilitySummary("000001", EActivityType.NEW, "TEST");

        domainModel.addAgreementDetails(agreementDetails);
        domainModel.addPersonalInfo(agreementDetails, ETraderType.SOLE_TRADER);

        var data = new AffordabilityDelphiDataResponse();
        var uuid = UUID.randomUUID();
        data.setUuid(uuid.toString());
        var delphiResponse = DelphiResponse.builder()
            .datetimeRequest("2022-01-01 00:00:00")
            .uuidInstance(UUID.randomUUID().toString())
            .data(data)
            .build();

        domainModel.addDelphiData(delphiResponse);

        assertAll(
            () -> assertThatNoException().isThrownBy(
                () -> domainModel.confirmOutcome(null, null, null, null, null)
            ),

            () -> assertEquals("000001", domainModel.agreementNumber),
            () -> assertEquals("TEST", domainModel.environment),
            () -> assertNotNull(domainModel.createdOn),

            () -> assertNotNull(domainModel.customer),
            () -> assertTrue(domainModel.customer.isNew),
            () -> assertEquals(PERSONAL, domainModel.customer.businessLine),

            () -> assertEquals("first name", domainModel.customer.forename),
            () -> assertEquals("middle name", domainModel.customer.middleName),
            () -> assertEquals("last name", domainModel.customer.surname),
            () -> assertEquals("title", domainModel.customer.title),

            () -> assertNotNull(domainModel.customer.address),
            () -> assertEquals("country", domainModel.customer.address.country),
            () -> assertEquals("street name", domainModel.customer.address.street),
            () -> assertEquals("post town", domainModel.customer.address.postTown),
            () -> assertEquals("0000-000", domainModel.customer.address.postcode),
            () -> assertEquals("house name", domainModel.customer.address.houseName),
            () -> assertEquals("house number", domainModel.customer.address.houseNumber),


            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(uuid.toString(), domainModel.affordabilityDelphi.uuidInstance),
            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(LocalDateTime
                    .of(2022, 1, 1, 0, 0, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.affordabilityDelphi.requestedOn),

            () -> assertEquals(BigDecimal.valueOf(10), domainModel.totalPremiumValue),
            () -> assertNull(domainModel.customerSegment),
            () -> assertNull(domainModel.consumerSummaryDelphi),
            () -> assertNull(domainModel.exclusionReason),
            () -> assertFalse(domainModel.isFunded),
            () -> assertNull(domainModel.effectiveDisposableSurplus),

            () -> assertEquals(dateOfBirth.atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.customer.dateOfBirth),
            () -> assertTrue(domainModel.customer.age < 18),
            () -> assertEquals(EStatus.ON_SCHEME, domainModel.status)
        );


    }

    @Test
    @DisplayName("Should return false when person is NOT under age")
    void shouldReturnFalseWhenPersonIsNotUnderAge() {
        var agreementDetails = new AgreementDetail();
        agreementDetails.setPersonSerial("00001");
        agreementDetails.setPersonTitle("title");
        agreementDetails.setPersonNameFirst("first name");
        agreementDetails.setPersonNameLast("last name");
        agreementDetails.setPersonNameMiddle("middle name");


        var dateOfBirth = LocalDateTime.now()
            .minusYears(30)
            .withNano(0);
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        agreementDetails.setPersonDateOfBirth(dateOfBirth.format(formatter));
        agreementDetails.setPersonHouseName("house name");
        agreementDetails.setPersonStreetName("street name");
        agreementDetails.setPersonPostCode("0000-000");
        agreementDetails.setPersonPostTown("post town");
        agreementDetails.setPersonHouseNumber("house number");
        agreementDetails.setAgrAgreementNumber("312312311");
        agreementDetails.setPersonCountry("country");
        agreementDetails.setAgrAdvance(BigDecimal.valueOf(10));

        var domainModel = new AffordabilitySummary("000001", EActivityType.NEW, "TEST");
        domainModel.addAgreementDetails(agreementDetails);
        domainModel.addPersonalInfo(agreementDetails, ETraderType.SOLE_TRADER);

        var data = new AffordabilityDelphiDataResponse();
        var uuid = UUID.randomUUID();
        data.setUuid(uuid.toString());

        var delphiResponse = DelphiResponse.builder()
            .datetimeRequest("2022-01-01 00:00:00")
            .uuidInstance(UUID.randomUUID().toString())
            .data(data)
            .build();
        domainModel.addDelphiData(delphiResponse);

        assertAll(
            () -> assertThatNoException()
                .isThrownBy(() -> domainModel.confirmOutcome(null, EStatus.NOT_ON_SCHEME,
                    null, null, null)),

            () -> assertEquals("000001", domainModel.agreementNumber),
            () -> assertEquals("TEST", domainModel.environment),
            () -> assertNotNull(domainModel.createdOn),

            () -> assertNotNull(domainModel.customer),
            () -> assertTrue(domainModel.customer.isNew),
            () -> assertEquals(PERSONAL, domainModel.customer.businessLine),


            () -> assertEquals("first name", domainModel.customer.forename),
            () -> assertEquals("middle name", domainModel.customer.middleName),
            () -> assertEquals("last name", domainModel.customer.surname),
            () -> assertEquals("title", domainModel.customer.title),


            () -> assertNotNull(domainModel.customer.address),
            () -> assertEquals("country", domainModel.customer.address.country),
            () -> assertEquals("street name", domainModel.customer.address.street),
            () -> assertEquals("post town", domainModel.customer.address.postTown),
            () -> assertEquals("0000-000", domainModel.customer.address.postcode),
            () -> assertEquals("house name", domainModel.customer.address.houseName),
            () -> assertEquals("house number", domainModel.customer.address.houseNumber),


            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(uuid.toString(), domainModel.affordabilityDelphi.uuidInstance),
            () -> assertNotNull(domainModel.affordabilityDelphi),
            () -> assertEquals(LocalDateTime
                    .of(2022, 1, 1, 0, 0, 0)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.affordabilityDelphi.requestedOn),

            () -> assertEquals(BigDecimal.valueOf(10), domainModel.totalPremiumValue),
            () -> assertNull(domainModel.customerSegment),
            () -> assertNull(domainModel.consumerSummaryDelphi),
            () -> assertNull(domainModel.exclusionReason),
            () -> assertFalse(domainModel.isFunded),
            () -> assertNull(domainModel.effectiveDisposableSurplus),

            () -> assertEquals(dateOfBirth.atZone(ZoneId.systemDefault())
                    .toInstant()
                , domainModel.customer.dateOfBirth),

            () -> assertFalse(domainModel.customer.age < 18),
            () -> assertEquals(EStatus.NOT_ON_SCHEME, domainModel.status)
        );


    }
}
