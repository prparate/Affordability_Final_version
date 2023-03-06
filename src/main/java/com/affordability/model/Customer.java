package com.affordability.model;

import com.affordability.customer.ETraderType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {
    public EBusinessLine businessLine;
    public Boolean isNew;
    public String companyName;
    public String companyNumber;
    public ETraderType traderType;
    public String title;
    public String surname;
    public String forename;
    public String middleName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    public Instant dateOfBirth;
    public Integer age;
    public Address address;
    public String numberOfPartners;

}
