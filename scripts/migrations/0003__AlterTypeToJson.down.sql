ALTER TABLE affordability_outcomes
ALTER COLUMN affordability_json_data  TYPE TEXT
USING affordability_json_data::TEXT;