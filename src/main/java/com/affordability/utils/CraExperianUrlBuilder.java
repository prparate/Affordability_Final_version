package com.affordability.utils;

import com.affordability.service.cra.request.CraRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CraExperianUrlBuilder {

    private UriComponentsBuilder uriBuilder;
    private final List<String> separators = Arrays.asList("", " ", ", ");
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public String buildCraExperianUrl(CraRequest agreementDetail, String craServiceUrl, String version, String operation) throws ParseException {

        initializeBaseUrl(craServiceUrl, version, operation);
        addCacheAndEnvironmentParamsIntoUrl();
        addPersonalDetailsIntoUrl(agreementDetail);
        addBankDetailsIntoUrl(agreementDetail);
        setPersonDetailsToUrl(agreementDetail);
        addTimeAtAddressIntoUrl(agreementDetail);

        return uriBuilder.toUriString();
    }

    private void initializeBaseUrl(String craBaseUrl, String version, String operation) {

        uriBuilder = UriComponentsBuilder.fromUriString(String.format("%s/%s/%s", craBaseUrl, version, operation));
    }

    private void addCacheAndEnvironmentParamsIntoUrl() {
        uriBuilder.queryParam("cache", "1")
                .queryParam("service_provider_environment", "default");
    }

    private void setPersonDetailsToUrl(CraRequest agreementDetail) throws IllegalArgumentException {
        addPersonAddressDetailsIntoUrl(agreementDetail);
        addPersonAddressIntoUrl(agreementDetail);
    }

    private void addPersonalDetailsIntoUrl(CraRequest agreementDetail) throws IllegalArgumentException {

        uriBuilder.queryParam("date_birth",
                        Optional.ofNullable(getPersonDateOfBirth(agreementDetail.getPersonDateOfBirth())).
                                orElseThrow(() -> new NotFoundException("Element not found with name: person_date_of_birth")))
                .queryParam("title",
                        Optional.ofNullable(agreementDetail.getPersonTitle()))
                .queryParam("name_first",
                        Optional.ofNullable(agreementDetail.getPersonNameFirst()).
                                orElseThrow(() -> new NotFoundException("Element not found with name: person_first_name")))
                .queryParam("name_last",
                        Optional.ofNullable(agreementDetail.getPersonNameLast()).
                                orElseThrow(() -> new NotFoundException("Element not found with name: person_last_name")))
                .queryParam("name_middle",
                        Optional.ofNullable(agreementDetail.getPersonNameMiddle()));
    }

    private LocalDate getPersonDateOfBirth(String dateTime) throws IllegalArgumentException {
        var flag = StringUtils.isNotBlank(dateTime);
        LocalDate localDate;
        if (flag) {
            try {
                var date = dateTime.substring(0, 10);
                localDate = LocalDate.parse(date);
            } catch (StringIndexOutOfBoundsException | DateTimeParseException e) {
                throw new IllegalArgumentException("Person Date Of Birth is not in correct format :" + dateTime);
            }
            return localDate;
        }
        return null;

    }

    private void addBankDetailsIntoUrl(CraRequest agreementDetail){

        uriBuilder.queryParam("bank_account_number",
                        Optional.ofNullable(agreementDetail.getBankAccountNumber()))
                .queryParam("bank_account_sort_code",
                        Optional.ofNullable(agreementDetail.getBankAccountSortCode()))
                .queryParam("amount",
                        Optional.ofNullable(agreementDetail.getAgrAdvance())
                                .orElse(new BigDecimal(0)));
    }

    private void addPersonAddressDetailsIntoUrl(CraRequest agreementDetail) throws NotFoundException {

        if (StringUtils.isBlank(agreementDetail.getPersonHouseNumber()) && StringUtils.isBlank(agreementDetail.getPersonHouseName())) {
            throw new NotFoundException("Person House Number and House Name not provided. One of the two should be provided.");
        } else {
            uriBuilder.queryParam("address_house_number",
                            Optional.ofNullable(agreementDetail.getPersonHouseNumber()))
                    .queryParam("address_house_name",
                            Optional.ofNullable(agreementDetail.getPersonHouseName()));
        }

        uriBuilder.queryParam("address_street_name",
                        Optional.ofNullable(agreementDetail.getPersonStreetName()))
                .queryParam("address_street_type",
                        Optional.ofNullable(agreementDetail.getPersonStreetType()))
                .queryParam("address_locality",
                        Optional.ofNullable(agreementDetail.getPersonLocality()))
                .queryParam("address_postcode",
                        Optional.ofNullable(agreementDetail.getPersonPostCode()).
                                orElseThrow(() -> new NotFoundException("Element not found with name: person_address_postcode")))
                .queryParam("address_town",
                        Optional.ofNullable(agreementDetail.getPersonPostTown()).
                                orElseThrow(() -> new NotFoundException("Element not found with name: person_address_town")))
                .queryParam("address_county",
                        Optional.ofNullable(agreementDetail.getPersonCountyCode()))
                .queryParam("address_country",
                        Optional.ofNullable(agreementDetail.getPersonCountry()));
    }

    private void addPersonAddressIntoUrl(CraRequest agreementDetail) {
        StringBuilder personAddress = new StringBuilder();
        var separatorIndex = 0;

        if (agreementDetail.getPersonHouseNumber() != null) {
            personAddress.append(agreementDetail.getPersonHouseNumber());
            separatorIndex = 1;
        }

        if (agreementDetail.getPersonHouseName() != null) {
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonHouseName());
            separatorIndex = 2;
        }

        if (agreementDetail.getPersonStreetName() != null) {
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonStreetName());
            separatorIndex = 1;
        }

        if (agreementDetail.getPersonStreetType() != null) {
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonStreetType());
            separatorIndex = 2;
        }

        if (agreementDetail.getPersonLocality() != null)
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonLocality());

        separatorIndex = 2;
        personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonPostCode());
        personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonPostTown());

        if (agreementDetail.getPersonCountyCode() != null)
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonCountyCode());

        if (agreementDetail.getPersonCountry() != null)
            personAddress.append(separators.get(separatorIndex)).append(agreementDetail.getPersonCountry());

        uriBuilder.queryParam("address", personAddress);

    }

    private void addTimeAtAddressIntoUrl(CraRequest agreementDetail) throws ParseException {

        if(StringUtils.isBlank(agreementDetail.habitationDateOut)){
            agreementDetail.habitationDateOut = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date habitationDateIn = dateFormat.parse(agreementDetail.habitationDateIn);
        Date habitationDateOut = dateFormat.parse(agreementDetail.habitationDateOut);
        long timeDiffInSeconds = (habitationDateOut.getTime()-habitationDateIn.getTime())/1000;

        uriBuilder.queryParam("time_at_address", buildTimeAtAddress(timeDiffInSeconds));

    }

    private String buildTimeAtAddress(long timeDiffInSeconds){

        var minutes = timeDiffInSeconds / 60;
        var hours = minutes / 60;
        var days = hours / 24;

        var hoursInStr = String.format("%02d", (hours % 24));
        var minutesInStr = String.format("%02d", (minutes % 60));
        var secondsInStr = String.format("%02d", (timeDiffInSeconds % 60));

        return "P"+days+"D"+"T"+hoursInStr+"H"+minutesInStr+"M"+secondsInStr+"S";
    }
}
