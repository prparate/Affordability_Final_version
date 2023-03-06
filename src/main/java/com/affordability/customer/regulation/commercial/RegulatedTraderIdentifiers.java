package com.affordability.customer.regulation.commercial;

import com.affordability.customer.ETraderType;
import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.IRegulatedCustomerIdentifier;
import com.affordability.customer.regulation.NullRegulatedCustomerIdentifier;
import com.affordability.customer.regulation.commercial.traders.*;
import com.affordability.customer.regulation.parameter.IRegulatedCustomerParameterProvider;

import java.util.EnumMap;

public class RegulatedTraderIdentifiers implements IRegulatedCustomerIdentifier {

    private final IRegulatedCustomerParameterProvider parameterProvider;
    private final EnumMap<ETraderType, ICommercialRegulatedCustomerIdentifier> identifiersByTraderType = new EnumMap<>(ETraderType.class);
    private CustomerDataForDetermineRegulation data;

    public RegulatedTraderIdentifiers(IRegulatedCustomerParameterProvider parameterProvider) {
        this.parameterProvider = parameterProvider;
        this.initializeIdentifiersByTraderTypes();
    }

    private void initializeIdentifiersByTraderTypes() {
        addAssociation();
        addCorporate();
        addLimitedCompany();
        addLimitedLiabilityPartnership();
        addLocalAuthority();
        addNHSTrust();
        addPartnership();
        addPLC();
        addRegisteredCharity();
        addSchool();
        addSoleTrader();
    }

    private void addLocalAuthority() {
        var identifier = new LocalAuthorityRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addLimitedLiabilityPartnership() {
        var identifier = new LimitedLiabilityPartnershipRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addLimitedCompany() {
        var identifier = new LimitedCompanyRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addNHSTrust() {
        var identifier = new NHSTrustRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addPLC() {
        var identifier = new PLCRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addSchool() {
        var identifier = new SchoolRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addRegisteredCharity() {
        var identifier = new RegisteredCharityRegulatedCustomerIdentifier(this.parameterProvider);
        addIdentifier(identifier);
    }

    private void addAssociation() {
        var identifier = new AssociationRegulatedCustomerIdentifier(this.parameterProvider);
        addIdentifier(identifier);
    }

    private void addPartnership() {
        var identifier = new PartnershipRegulatedCustomerIdentifier(this.parameterProvider);
        addIdentifier(identifier);
    }

    private void addSoleTrader() {
        var identifier = new SoleTraderRegulatedCustomerIdentifier(this.parameterProvider);
        addIdentifier(identifier);
    }

    private void addCorporate() {
        var identifier = new CorporateRegulatedCustomerIdentifier();
        addIdentifier(identifier);
    }

    private void addIdentifier(ICommercialRegulatedCustomerIdentifier identifier) {
        this.identifiersByTraderType.put(identifier.getTraderType(), identifier);
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        this.data = data;
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        var identifier = this.getIdentifierByTraderOrDefault();
        identifier.setCustomerDataForRegulation(this.data);

        return identifier.isThisARegulatedCustomer();
    }

    private IRegulatedCustomerIdentifier getIdentifierByTraderOrDefault() {
        var key = this.data.getTraderType();
        IRegulatedCustomerIdentifier identifier;

        if (this.identifiersByTraderType.containsKey(key)) {
            identifier = this.identifiersByTraderType.get(key);
        } else {
            identifier = new NullRegulatedCustomerIdentifier();
        }

        return identifier;
    }

}