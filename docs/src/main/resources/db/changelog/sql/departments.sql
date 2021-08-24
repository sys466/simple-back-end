create table docs.departments
(
	code text not null
		constraint departments_pk
			primary key,
	description text not null
);

alter table docs.departments owner to "user";

create unique index departments_description_uindex
	on docs.departments (description);

INSERT INTO docs.departments (code, description)
VALUES ('DEP-FNS', 'FNS department');

INSERT INTO docs.departments (code, description)
VALUES ('DEP-PFR', 'PFR department');

