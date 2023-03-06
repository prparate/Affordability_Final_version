package com.affordability.dto;
import com.affordability.model.EActivityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EActivityTypeTest {

    @ParameterizedTest
    @ValueSource(strings={"new_business","mta","renewal", "NEW_BUSINESS","MTA","RENEWAL", "New_Business","Mta","Renewal" })
    @DisplayName("Should Return true when event activity is valid")
    void shouldReturnTrueWhenEventActivityIsValid(String text){
        assertTrue(EActivityType.isValid(text));

        var activityType = EActivityType.fromTextToValue(text);
        Assert.notNull(activityType, "Activity type '" + text +"' should be found by text");
    }

    @Test
    @DisplayName("Should Return false when when event activity is not valid")
    void shouldReturnFalseWhenEventActivityIsNotValid(){
        assertFalse(EActivityType.isValid("random"));
    }
}
