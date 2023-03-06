/*report affordability_outcomes*/
ALTER TABLE affordability_outcomes
    RENAME COLUMN reason to exclusion_reason;

ALTER TABLE affordability_outcomes
    ADD IF NOT EXISTS final_decision varchar(20);

ALTER TABLE affordability_outcomes
    ADD IF NOT EXISTS inclusion_outcome varchar(20);

ALTER TABLE affordability_outcomes
    ADD IF NOT EXISTS automated_decision varchar(20);

ALTER TABLE affordability_outcomes
    ADD IF NOT EXISTS final_create_on timestamp ;



/*report table*/
ALTER TABLE report_data
    RENAME COLUMN reason to exclusion_reason;

ALTER TABLE report_data
    ADD IF NOT EXISTS final_decision varchar(20);

ALTER TABLE report_data
    ADD IF NOT EXISTS inclusion_outcome varchar(20);

ALTER TABLE report_data
    ADD IF NOT EXISTS automated_decision varchar(20);

ALTER TABLE report_data
    ADD IF NOT EXISTS final_create_on timestamp ;

ALTER TABLE report_data
    ADD IF NOT EXISTS number_of_partners INT;

ALTER TABLE report_data
    ADD IF NOT EXISTS age int;
