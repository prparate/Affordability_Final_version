alter table error_cra_update add IF not EXISTS status varchar(20) not null default 'PENDING';

update error_cra_update set status = 'PENDING' WHERE updated is false;
update error_cra_update set status = 'PENDING' WHERE updated is null;
update error_cra_update set status = 'COMPLETED' WHERE updated is true;

alter table error_cra_update drop IF  EXISTS updated;