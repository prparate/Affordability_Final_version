/*report affordability_outcomes*/
ALTER TABLE affordability_outcomes
    RENAME COLUMN exclusion_reason to reason;

ALTER TABLE affordability_outcomes
    DROP IF EXISTS final_decision;

ALTER TABLE affordability_outcomes
    DROP IF EXISTS inclusion_outcome ;

ALTER TABLE affordability_outcomes
    DROP IF EXISTS automated_decision ;

ALTER TABLE affordability_outcomes
    DROP IF EXISTS final_create_on ;



/*report table*/
ALTER TABLE report_data
    RENAME COLUMN exclusion_reason to reason;

ALTER TABLE report_data
    DROP IF EXISTS final_decision ;

ALTER TABLE report_data
    DROP IF EXISTS inclusion_outcome ;

ALTER TABLE report_data
    DROP IF EXISTS automated_decision ;

ALTER TABLE report_data
    DROP IF EXISTS final_create_on ;

ALTER TABLE report_data
    DROP IF EXISTS number_of_partners;

ALTER TABLE report_data
    DROP IF EXISTS age;
