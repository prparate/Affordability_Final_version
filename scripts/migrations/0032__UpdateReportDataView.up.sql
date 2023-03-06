DROP VIEW IF EXISTS  report_data;
CREATE VIEW report_data as
select
    id,
    id as outcome_id,
    activity_type ,
    environment ,
    created_on ,
    affordability_json_data ->> 'effective_disposable_surplus' as effective_disposable_surplus,
    cast(affordability_json_data ->> 'total_premium_value' as decimal) as total_premium_value,
    cast(affordability_json_data ->> 'is_funded' as boolean) as is_funded,
    cast(affordability_json_data ->> 'is_regulated' as boolean) as is_regulated,
    affordability_json_data ->> 'exclusion_reason' as exclusion_reason,
    affordability_json_data ->> 'customer_segment' as customer_segment,
    affordability_json_data ->> 'status' as status,
    affordability_json_data ->> 'final_decision' as decision_type,
    (affordability_json_data ->> 'customer')::json ->> 'business_line' as business_line,
    cast((affordability_json_data ->> 'customer')::json ->> 'is_new' as boolean) as is_new,
    trim((affordability_json_data ->> 'customer')::json ->> 'company_name') as company_name,
    (affordability_json_data ->> 'customer')::json ->> 'company_number' as company_number,
    (affordability_json_data ->> 'customer')::json ->> 'trader_type' as trader_type,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'street') as address_street,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'country') as address_country,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'post_town') as address_post_town,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'postcode') as address_postcode,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'house_name') as address_house_name,
    trim(((affordability_json_data ->> 'customer')::json ->> 'address')::json ->> 'house_number') as address_house_number,
    trim((affordability_json_data ->> 'customer')::json ->> 'title') as personal_title,
    trim((affordability_json_data ->> 'customer')::json ->> 'surname') as personal_surname,
    trim((affordability_json_data ->> 'customer')::json ->> 'forename') as personal_forename,
    trim((affordability_json_data ->> 'consumer_summary_delphi')::json ->>'middle_name') as personal_middle_name,
    cast((affordability_json_data ->> 'customer')::json ->> 'date_of_birth' as timestamp) as personal_date_of_birth,
    cast((affordability_json_data ->> 'affordability_delphi')::json ->> 'requestedOn' as timestamp) as delphi_requested_on,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'uuidInstance' as delphi_uuid_instance,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'NDSPCII' as delphi_data_ndspcii,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'E1B07' as delphi_data_e1b07,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'E1B08' as delphi_data_e1b08,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'EA1C01' as delphi_data_ea1c01,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'EA4Q05' as delphi_data_ea4q05,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'E1A07' as delphi_data_e1a07,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'E1D02' as delphi_data_e1d02,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'E1B09' as delphi_data_e1b09,
    (affordability_json_data ->> 'affordability_delphi')::json ->> 'NDECC03' as delphi_data_ndecc03,
    cast((affordability_json_data ->> 'consumer_summary_delphi')::json ->>'requestedOn' as timestamp) as consumer_requested_on,
    (affordability_json_data ->> 'consumer_summary_delphi')::json ->> 'uuidInstance' as consumer_uuid_instance,
    (affordability_json_data ->> 'consumer_summary_delphi')::json ->> 'SPEDI03' as consumer_data_spedi03,
    (affordability_json_data ->> 'consumer_summary_delphi')::json ->> 'SPEDI04' as consumer_data_spedi04,
    (affordability_json_data ->> 'consumer_summary_delphi')::json ->> 'SPEDI05' as consumer_data_spedi05,
    (affordability_json_data ->> 'consumer_summary_delphi')::json ->> 'SPEDI08' as consumer_data_spedi08,
    affordability_json_data ->> 'final_decision' as final_decision,
    affordability_json_data ->> 'inclusion_outcome' as inclusion_outcome,
    affordability_json_data ->> 'automated_decision' as automated_decision,
    cast(affordability_json_data ->> 'final_created_on' as timestamp) as final_created_on,
    (affordability_json_data ->> 'customer')::json ->> 'number_of_partners' as number_of_partners,
    cast((affordability_json_data ->> 'customer')::json ->> 'age' as integer ) as age,
    affordability_json_data ->> 'status_before' as status_before,
    affordability_json_data ->> 'agreement_number' as agreement_number,
    affordability_json_data ->> 'low_instalment' as low_instalment,
    affordability_json_data ->> 'income_expenditure' as income_expenditure,
    cast(affordability_json_data ->> 'agreement_serial' as integer) as agreement_serial,
    cast(affordability_json_data ->> 'finance_instalment_amount' as numeric) as finance_instalment_amount,
    affordability_json_data ->> 'incorporation_status' as incorporation_status,
    cast(affordability_json_data ->> 'agreement_created_on' as timestamp) as agreement_created_on,
    cast(affordability_json_data ->> 'automated_decision_date' as timestamp) as automated_decision_date,
    affordability_json_data ->> 'final_scheme_assigned' as final_scheme_assigned,
    affordability_json_data ->> 'automated_scheme_assigned' as automated_scheme_assigned
from affordability_outcomes ao ;