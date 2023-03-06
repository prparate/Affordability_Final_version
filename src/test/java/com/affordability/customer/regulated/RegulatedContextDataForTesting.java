package com.affordability.customer.regulated;

import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.parameter.IRegulatedCustomerParameterProvider;

import java.math.BigDecimal;

public class RegulatedContextDataForTesting implements ParamLimits, ClientValues {

    private RegulatedCustomerParameterProviderMock provider = new RegulatedCustomerParameterProviderMock();
    private CustomerDataForDetermineRegulation customerData = new CustomerDataForDetermineRegulation();

    public IRegulatedCustomerParameterProvider getProvider() {
        return this.provider;
    }

    public CustomerDataForDetermineRegulation getCustomerData() {
        return this.customerData;
    }

    @Override
    public ParamLimits maxPolicyValueAllowed(double value) {
        provider.getMaxPolicyValueAllowedReturn = value;
        return this;
    }

    @Override
    public ParamLimits maxNumberOfPartnersAllowed(int value) {
        provider.getMaxNumberOfPartnersAllowedReturn = value;
        return this;
    }

    public ParamLimits withParams() {
        return this;
    }

    @Override
    public ClientValues numberOfPartners(int value) {
        customerData.setNumberOfPartners(value);
        return this;
    }

    @Override
    public ClientValues policyValue(double value) {
        customerData.setPolicyValue(new BigDecimal(value));
        return this;
    }

    public ClientValues withCustomer(){
        return this;
    }

}

