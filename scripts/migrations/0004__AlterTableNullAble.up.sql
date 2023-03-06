ALTER TABLE affordability_outcomes
ALTER COLUMN  customer_type DROP NOT NULL,
    ALTER COLUMN  activity_type DROP NOT NULL,
    ALTER COLUMN  decision_type DROP NOT NULL,
    ALTER COLUMN  segmentation DROP NOT NULL,
    ALTER COLUMN  reason DROP NOT NULL,
    ALTER COLUMN  outcome DROP NOT NULL;