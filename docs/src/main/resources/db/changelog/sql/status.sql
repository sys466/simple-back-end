create table docs.status
(
	code text not null
		constraint status_pk
			primary key,
	description text not null
);

alter table docs.status owner to "user";

create unique index status_description_uindex
	on docs.status (description);

INSERT INTO docs.status (code, description)
VALUES ('NEW', 'Statement registered');

INSERT INTO docs.status (code, description)
VALUES ('PROCESSING', 'Statement forwarded to the department');

INSERT INTO docs.status (code, description)
VALUES ('COMPLETED', 'Statement processed by the department');

