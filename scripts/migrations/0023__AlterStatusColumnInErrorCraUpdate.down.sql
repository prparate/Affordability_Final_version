alter table error_cra_update add IF not EXISTS updated boolean not null default false;

update error_cra_update set updated = false WHERE status = 'PENDING';
update error_cra_update set updated = true WHERE status in ('COMPLETED','ERROR');

alter table error_cra_update drop IF  EXISTS  status;