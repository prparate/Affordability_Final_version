package com.affordability.customer.regulated;

public interface ParamLimits {
    ParamLimits maxPolicyValueAllowed(double value);
    ParamLimits maxNumberOfPartnersAllowed(int value);
}
