ALTER TABLE affordability_outcomes
ALTER COLUMN affordability_json_data  TYPE JSON
USING affordability_json_data::JSON;