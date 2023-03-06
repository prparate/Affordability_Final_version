ALTER TABLE report_data
DROP IF EXISTS agreement_created_on;

ALTER TABLE report_data
DROP IF EXISTS automated_decision_date;


ALTER TABLE report_data
DROP IF EXISTS final_scheme_assigned;

ALTER TABLE report_data
DROP IF EXISTS automated_scheme_assigned;


ALTER TABLE report_data
    add IF not EXISTS address_flat varchar(20) ;
