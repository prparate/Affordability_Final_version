package com.affordability.customer.regulation;

public interface ICustomerDataForRegulationProvider {
    CustomerDataForDetermineRegulation findData(String agreementNumber);
}
