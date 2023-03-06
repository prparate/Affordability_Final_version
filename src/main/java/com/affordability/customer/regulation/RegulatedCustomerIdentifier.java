package com.affordability.customer.regulation;

import com.affordability.customer.regulation.commercial.RegulatedTraderIdentifiers;
import com.affordability.customer.regulation.parameter.IRegulatedCustomerParameterProvider;
import com.affordability.customer.regulation.personal.RegulatedPersonalCustomerIdentifier;
import com.affordability.model.EBusinessLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
@Slf4j
public class RegulatedCustomerIdentifier implements IRegulatedCustomerIdentifier {

    private final IRegulatedCustomerIdentifier traderIdentifiers;
    private final IRegulatedCustomerIdentifier personalIdentifier;

    private CustomerDataForDetermineRegulation data;

    @Autowired
    public RegulatedCustomerIdentifier(IRegulatedCustomerParameterProvider parameterProvider) {
        this.traderIdentifiers = new RegulatedTraderIdentifiers(parameterProvider);
        this.personalIdentifier = new RegulatedPersonalCustomerIdentifier();
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        var identifier = this.getIdentifier();
        identifier.setCustomerDataForRegulation(this.data);

        return identifier.isThisARegulatedCustomer();
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        this.data = data;
    }

    private IRegulatedCustomerIdentifier getIdentifier() {

        if (isATrader()) {
            return this.traderIdentifiers;
        }

        if (isPersonal()) {
            return this.personalIdentifier;
        }

        return new NullRegulatedCustomerIdentifier();
    }

    private boolean isPersonal() {
        return this.data.getBusinessLine() == EBusinessLine.PERSONAL;
    }

    private boolean isATrader() {
        return this.data.getTraderType() != null;
    }
}
