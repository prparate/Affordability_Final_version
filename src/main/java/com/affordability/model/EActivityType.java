package com.affordability.model;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.upperCase;

@Getter
public enum EActivityType {
    NEW("new_business"),
    RENEWAL("Renewal"),
    MTA("MTA"), //Mid Term Adjustment
    ;

    private final String text;

    EActivityType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static boolean isValid(String text) {
        return nameToValue.containsKey(lowerCase(text));
    }

    public static EActivityType fromTextToValue(String activity) throws IllegalArgumentException {
        var activityToCheck = lowerCase(activity);

        if (isValid(activityToCheck)) {
            return nameToValue.get(activityToCheck);
        }

        throw new IllegalArgumentException("EActivityType '" + activityToCheck + "' is not supported");
    }

    private static final HashMap<String, EActivityType> nameToValue;

    static {
        var stream = Stream.of(values());
        nameToValue = stream
                .collect(toMap(entry -> lowerCase(entry.getText()), Function.identity(), (prev, next) -> next, HashMap::new));
    }
}
