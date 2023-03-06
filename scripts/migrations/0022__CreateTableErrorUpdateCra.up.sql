CREATE TABLE IF NOT EXISTS error_cra_update (
    outcome_id uuid PRIMARY KEY NOT NULL,
    created_at timestamp NOT NULL,
    updated boolean default false,
    error_message varchar(255) NOT NULL
    )