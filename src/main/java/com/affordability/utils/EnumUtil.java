package com.affordability.utils;

import com.affordability.model.Customer;
import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class EnumUtil {

    public static String getEnumName(Enum<?> item) {
        return Optional.ofNullable(item)
                .map(Enum::name)
                .orElse(null);
    }

    public static String getCustomerTypeString(Customer customer) {
        return customer != null && customer.businessLine != null ? getEnumName(customer.businessLine) : null;
    }
}
