ALTER TABLE report_data
    add IF not EXISTS agreement_created_on  timestamp ;

ALTER TABLE report_data
    add IF not EXISTS automated_decision_date  timestamp ;


ALTER TABLE report_data
    add IF not EXISTS final_scheme_assigned  varchar(20) ;

ALTER TABLE report_data
    add IF not EXISTS automated_scheme_assigned  varchar(20) ;


ALTER TABLE report_data
    DROP IF EXISTS address_flat ;
