create table docs.types
(
	code text not null
		constraint types_pk
			primary key,
	description text not null
);

alter table docs.types owner to "user";

create unique index types_description_uindex
	on docs.types (description);

INSERT INTO docs.types (code, description)
VALUES ('DOC-FNS', 'Statement to FNS department');

INSERT INTO docs.types (code, description)
VALUES ('DOC-PFR', 'Statement to PFR department');

