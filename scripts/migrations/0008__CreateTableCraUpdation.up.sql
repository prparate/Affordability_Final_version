CREATE TABLE IF NOT EXISTS cra_updation (
    id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    agreement_number varchar(20) NOT NULL,
    status varchar(20) NOT NULL,
    environment varchar(20) NOT NULL,
    updated boolean NOT NULL,
    updated_on timestamp
    )