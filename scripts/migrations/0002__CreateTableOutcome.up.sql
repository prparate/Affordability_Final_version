CREATE TABLE IF NOT EXISTS affordability_outcomes (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    agreement_number varchar(20) NOT NULL,
    customer_type varchar(15) NOT NULL,
    activity_type varchar(15) NOT NULL,
    decision_type varchar(30) NOT NULL,
    segmentation varchar(20) NOT NULL,
    reason varchar(20) NOT NULL,
    create_on timestamp NOT NULL,
    affordability_json_data TEXT NOT NULL,
    outcome varchar(20) NOT NULL
)