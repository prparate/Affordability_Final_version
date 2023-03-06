CREATE TABLE IF NOT EXISTS report_data (

    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    outcome_id uuid NOT null,
    activity_type VARCHAR(50),
    environment VARCHAR(50),
    created_on TIMESTAMP NOT NULL,
    effective_disposable_surplus decimal(10,2),
    total_premium_value decimal(10,2),
    is_funded BOOLEAN default  false,
    is_regulated BOOLEAN default  false,
    reason varchar(255),
    customer_segment varchar(100),
    status varchar(100),
    decision_type varchar(100),

    /*domainModel.customer*/
    business_line varchar(100),
    is_new BOOLEAN default false,

    /*domainModel.customer.commercial*/
    company_name varchar(255),
    company_number varchar(255),
    company_trader_type varchar(255),

    /*domainModel.customer.address*/
    address_flat varchar(255),
    address_street varchar(255),
    address_country varchar(255),
    address_post_town varchar(255),
    address_postcode varchar(255),
    address_house_name varchar(255),
    address_house_number varchar(255),


    /*domainModel.customer.personal*/
    personal_title varchar(255),
    personal_surname varchar(255),
    personal_forename varchar(255),
    personal_middle_name varchar(255),
    personal_date_of_birth timestamp,


    /*domainModel.affordabilityDelphi*/
    delphi_requested_on timestamp,
    delphi_uuid_instance varchar(255),

    /*domainModel.affordabilityDelphi.data*/
    delphi_data_ndspcii varchar(255),
    delphi_data_e1b07 varchar(255),
    delphi_data_e1b08 varchar(255),
    delphi_data_ea1c01 varchar(255),
    delphi_data_ea4q05 varchar(255),
    delphi_data_e1a07 varchar(255),
    delphi_data_e1d02 varchar(255),
    delphi_data_e1b09 varchar(255),
    delphi_data_ndecc03 varchar(255),


    /*domainModel.consumerSummaryDelphi*/
    consumer_requested_on timestamp,
    consumer_uuid_instance varchar(255),


    /*domainModel.consumerSummaryDelphi.data*/
    consumer_data_spedi03 varchar(255),
    consumer_data_spedi04 varchar(255),
    consumer_data_spedi05 varchar(255),
    consumer_data_spedi06 varchar(255),
    consumer_data_spedi08 varchar(255)

    );