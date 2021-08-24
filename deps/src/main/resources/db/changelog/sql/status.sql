create table deps.status
(
	code text not null
		constraint status_pk
			primary key,
	description text not null
);

alter table deps.status owner to "user";

create unique index status_description_uindex
	on deps.status (description);

INSERT INTO deps.status (code, description)
VALUES ('WAIT', 'Statement awaits of processing');

INSERT INTO deps.status (code, description)
VALUES ('COMPLETED', 'Statement processed by the department');



