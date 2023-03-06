ALTER TABLE  affordability_outcomes
ALTER COLUMN  customer_type SET NOT NULL,
    ALTER COLUMN  activity_type SET NOT NULL,
    ALTER COLUMN  decision_type SET NOT NULL,
    ALTER COLUMN  segmentation SET NOT NULL,
    ALTER COLUMN  reason SET NOT NULL,
    ALTER COLUMN  outcome SET NOT NULL;