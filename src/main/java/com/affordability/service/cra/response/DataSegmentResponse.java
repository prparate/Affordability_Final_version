package com.affordability.service.cra.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSegmentResponse implements Serializable {

    private String title;
    private String value;

    @JsonIgnore
    public boolean isValueNullOrEmpty() {
        return isBlank(this.value);
    }
    @JsonIgnore
    public boolean isValueEqual(String to) {
		return equalsIgnoreCase(this.value, to);
    }
    @JsonIgnore
    public boolean isValueAnInteger() {
        return (getValueAsIntegerOrNull() != null);
    }
    @JsonIgnore
    public Integer getValueAsInteger() {
        return Integer.parseInt(this.value);
    }
    @JsonIgnore
    public Integer getValueAsIntegerOrNull() {
        try {
            return this.getValueAsInteger();
        } catch (NumberFormatException e) {
            return null;
        }
    }
    @JsonIgnore
    public Integer getValueAsIntegerOrDefault() {
        try {
            return this.getValueAsInteger();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @JsonIgnore
    public BigDecimal getValueAsBigDecimal() {
        try {
            return new BigDecimal(this.value);
        } catch (NullPointerException | NumberFormatException e) {
            return null;
        }
    }


}
